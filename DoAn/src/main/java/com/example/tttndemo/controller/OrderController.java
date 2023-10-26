package com.example.tttndemo.controller;



import com.example.tttndemo.authentication.MyUserDetails;
import com.example.tttndemo.entity.*;
import com.example.tttndemo.exception.OrderNotFoundException;
import com.example.tttndemo.exception.OrderStatusNotFoundException;
import com.example.tttndemo.exception.UserNotFoundException;
import com.example.tttndemo.export.OrderDetailExport;
import com.example.tttndemo.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;
    private final CartItemService cartItemService;
    private final OrderStatusService orderStatusService;
    private final AddressService addressService;

    private final ProductService productService;

    public OrderController(UserService userService, OrderService orderService, CartItemService cartItemService, OrderStatusService orderStatusService, AddressService addressService, ProductService productService) {
        this.userService = userService;
        this.orderService = orderService;
        this.cartItemService = cartItemService;
        this.orderStatusService = orderStatusService;
        this.addressService = addressService;
        this.productService = productService;
    }

    @GetMapping("/order")
    public String getListOrder(@RequestParam(required = false, value = "type") Integer type, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);

        List<Order> orderList;
        if(type == null){
            orderList = orderService.getAllOrder(user,null);
        }else orderList = orderService.getAllOrder(user,type);
        model.addAttribute("type",type);
        model.addAttribute("orderList",orderList);

        return "/order/orders";
    }

    @GetMapping("/order/{number}")
    public String getListOrderByType(@PathVariable(name = "number") Integer orderId, Model model)
            throws OrderNotFoundException {

        Order order = orderService.getOrder(orderId);
        model.addAttribute("order", order);
        return "order/order_detail";
    }

    @GetMapping("/checkout")
    public String getcheckOut(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        List<CartItem> itemList = cartItemService.listCartItems(user);
        if( itemList.isEmpty()){
            return "cart";
        }
        Address address = addressService.getDefaultAddress(user);
        List<Address> addressList = addressService.getListAddress(user.getId());

        model.addAttribute("pageTitle","Check out");
        model.addAttribute("itemList",itemList);
        model.addAttribute("address",address);
        model.addAttribute("addressList",addressList);
        return "checkout";
    }

    @GetMapping("/order1")
    public String getOrderSuccessDetail(Model model) {
        model.addAttribute("pageTitle", "Order detail");
        return "order_detail";
    }

    @GetMapping("/order/{id}/request-cancel")
    @ResponseBody
    public String requestCancel(@PathVariable("id") Integer id)  {
        try {
            Order order = orderService.getOrder(id);
            OrderStatus orderStatus = new OrderStatus(5);
            order.setOrderStatus(orderStatus);
            orderService.saveOrder(order);
            return "success change order status";
        }catch (OrderNotFoundException ex){
            return "fail";
        }
    }

    @PostMapping("/order/purchase/{addressID}")
    @ResponseBody
    public String purchaseOrder(@PathVariable("addressID") Integer addressId)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        Order order =  orderService.createOrder(addressId, user.getId(), null);
        if(order.getId() == null){
            return "order fail";
        }
        cartItemService.removeAllCartItem(user.getId());
        return "order success";

    }

    @GetMapping("/order/check-quantity")
    @ResponseBody
    public Boolean checkQuantityProductToCreateOrder(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        List<CartItem> itemList = cartItemService.listCartItems(user);
        boolean result = true;
        for(CartItem c : itemList){
            if (c.getQuantity() > c.getProduct().getInStock()){
                result = false;
            }
        }
        return result;
    }

    @GetMapping("/order/payment/success")
    public String getOnlinePaymentSuccessPage(
            @RequestParam("vnp_TransactionNo") String vnp_TransactionNo
            , @RequestParam("vnp_Amount") Long vnp_Amount
            , @RequestParam("vnp_BankCode") String BankCode
            , @RequestParam("vnp_BankTranNo") String BankTranNo
            , @RequestParam("vnp_PayDate") String PayDate
            , @RequestParam("vnp_TxnRef") String txnRef
            , @RequestParam("vnp_OrderInfo") String orDerInfo
            , @RequestParam("vnp_ResponseCode") String vnp_ResponseCode
            , @RequestParam("vnp_TransactionStatus") String vnp_TransactionStatus
            , HttpSession session, Model model
            ,@AuthenticationPrincipal MyUserDetails userDetails) throws UserNotFoundException {

        User user = userService.getUserByEmail(userDetails.getUsername());

        long totalPrice = vnp_Amount/100;

        model.addAttribute("vnp_TransactionNo",vnp_TransactionNo);
        model.addAttribute("vnp_Amount",totalPrice);
        model.addAttribute("BankCode",BankCode);
        model.addAttribute("BankTranNo",BankTranNo);
        model.addAttribute("PayDate",PayDate);
        model.addAttribute("txnRef",txnRef);
        model.addAttribute("orDerInfo",orDerInfo);
        model.addAttribute("vnp_ResponseCode",vnp_ResponseCode);
        model.addAttribute("vnp_TransactionStatus",vnp_TransactionStatus);

        Integer addressId =(Integer) session.getAttribute("addressId");
        if(vnp_TransactionStatus.equals("00")){
            orderService.createOrder(addressId, user.getId(),BankTranNo);
            cartItemService.removeAllCartItem(user.getId());
        }

        return "payment_status";

    }


    @GetMapping("/admin/order")
    public String listOrderAdminFirstPage() {
            return "redirect:/admin/order/page/1";
    }

    @GetMapping("/admin/order/page/{pageNum}")
    public String listOrderAdminPage(@AuthenticationPrincipal MyUserDetails loggedUser, Model model,
                                     @PathVariable(name = "pageNum") Integer pageNum,
                                     @RequestParam(name = "status", required = false) String status,
                                     RedirectAttributes redirectAttributes) {
        Integer id = loggedUser.getId();
        try {
            User user = userService.getUserByID(id);


            if(status == null) {
                status = "ALL";
            }

            Page<Order> page = orderService.listByPage(pageNum ,status);
            List<Order> listOrders = page.getContent();


            long startCount = (long) (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
            long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
            if (endCount > page.getTotalElements()) {
                endCount = page.getTotalElements();
            }
            List<OrderStatus> orderStatusList = orderStatusService.listOrderStatus();

            model.addAttribute("orderStatusList", orderStatusList);
            model.addAttribute("user", user);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("listOrders", listOrders);
            model.addAttribute("status", status);
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);

            return "order/orders_admin";
        }
        catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/admin";
        }
    }

    @GetMapping("/admin/order/accept")
    public String acceptOrder(@RequestParam("id") Integer id, @RequestParam("statusId") Integer statusId) throws OrderNotFoundException {

        orderService.acceptOrder(id, statusId);
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/deny")
    public String denyOrder(@RequestParam("id") Integer id, @RequestParam("statusId") Integer statusId) throws OrderNotFoundException {

        orderService.denyOrder(id, statusId);
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/detail/{orderId}")
    public String getAdminOrderDetail(@PathVariable(name = "orderId") Integer orderId, Model model)
            throws OrderNotFoundException {

        Order order = orderService.getOrder(orderId);
        List<OrderStatus> orderStatusList = orderStatusService.listOrderStatus();
        model.addAttribute("order", order);
        model.addAttribute("orderStatusList",orderStatusList);
        return "order/order_detail_admin";
    }

    @GetMapping("/admin/order/{orderId}/changestatus/{statusId}")
    @ResponseBody
    public String getAdminOrderDetail(@PathVariable(name = "orderId") Integer orderId,
                                      @PathVariable(name = "statusId") Integer statusId) {

        try {
            Order order = orderService.getOrder(orderId);
            OrderStatus orderStatus = orderStatusService.getOrderStatusById(statusId);
            order.setOrderStatus(orderStatus);
            orderService.saveOrder(order);
            if(order.getOrderStatus().getId() == 4){
                for(OrderDetail orderDetail : order.getOrderDetails()){
                    Product product = orderDetail.getProduct();
                    product.setInStock(orderDetail.getProduct().getInStock() - orderDetail.getQuantity());
                    product.setSoldQuantity(orderDetail.getProduct().getSoldQuantity() + orderDetail.getQuantity());
                    productService.saveProduct(product);
                }
            }else if(order.getOrderStatus().getId() == 6){
                for(OrderDetail orderDetail : order.getOrderDetails()){
                    Product product = orderDetail.getProduct();
                    product.setInStock(orderDetail.getProduct().getInStock() + orderDetail.getQuantity());
                    product.setSoldQuantity(orderDetail.getProduct().getSoldQuantity() - orderDetail.getQuantity());
                    productService.saveProduct(product);
                }
            }
        }catch (OrderNotFoundException e) {
            return "change order status fail";
        }
        catch (OrderStatusNotFoundException ex){
            return "change order status fail";
        }
        return "change order status success";
    }

    @GetMapping("/admin/order/pdf")
    @ResponseBody
    public void exportToPDF(@RequestParam("id") Integer id, HttpServletResponse response) throws OrderNotFoundException, IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        String headerKey = "Content-Disposition";
        String headerValue = "inline; filename=order-detail-id=" + id + ".pdf";
        response.setHeader(headerKey, headerValue);

        Order order = orderService.getOrder(id);

        OrderDetailExport exporter = new OrderDetailExport(order);
        exporter.export(response);
    }
}
