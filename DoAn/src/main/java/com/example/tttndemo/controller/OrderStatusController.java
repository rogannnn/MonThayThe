package com.example.tttndemo.controller;


import com.example.tttndemo.entity.OrderStatus;
import com.example.tttndemo.exception.OrderNotFoundException;
import com.example.tttndemo.exception.OrderStatusNotFoundException;
import com.example.tttndemo.service.OrderStatusService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    public OrderStatusController(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    @GetMapping("/admin/orderstatus")
    public String listFirstPage() {
        return "redirect:/admin/orderstatus/page/1";
    }

    @GetMapping("/admin/orderstatus/page/{pageNum}")
    public String getOrderStatusPage(@PathVariable(name = "pageNum") Integer pageNum, Model model,
                                     @RequestParam(defaultValue = "id") String sortField,
                                     @RequestParam(required = false) String sortDir) {

        model.addAttribute("sortField", sortField);

        if(sortDir == null) sortDir = "asc";
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);


        Page<OrderStatus> page = orderStatusService.listByPage(pageNum, sortDir, sortField);
        List<OrderStatus> listOrderStatus = page.getContent();
        long startCount = (pageNum - 1) * orderStatusService.ORDERSTATUS_PER_PAGE + 1;
        long endCount = startCount +  orderStatusService.ORDERSTATUS_PER_PAGE - 1;

        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }



        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);

        model.addAttribute("listOrderStatus", listOrderStatus);

        return "order_status/order-status";
    }

    @PostMapping("/admin/orderstatus/new")
    @ResponseBody
    public String newOrderStatus(@RequestParam("name") String name){
        try {
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setName(name);
            orderStatusService.saveOrderStatus(orderStatus);
            return "add new order status success";
        }catch (RuntimeException ex){
            return ex.getMessage();
        }
    }

    @PostMapping("/admin/orderstatus/edit/{id}")
    @ResponseBody
    public String editOrderStatus(@PathVariable("id") Integer id,
            @RequestParam("name") String name){
            try{
                OrderStatus orderStatus = orderStatusService.getOrderStatusById(id);
                orderStatus.setName(name);
                orderStatusService.saveOrderStatus(orderStatus);
                return "edit order status success";
            } catch (OrderStatusNotFoundException e) {
                return "edit order status fail";
            }
    }


    @GetMapping("/admin/orderstatus/delete/{id}")
    public String deleteOrderStatus(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            orderStatusService.deleteOrderStatus(id);
            redirectAttributes.addFlashAttribute("messageSuccess", "The order status ID " + id + " has been deleted successfully");
        }
        catch (OrderNotFoundException ex) {
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/admin/orderstatus/page/1";
    }
}
