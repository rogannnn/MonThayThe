package com.example.tttndemo.service;

import com.example.tttndemo.dto.CartDTO;
import com.example.tttndemo.dto.ProductDTO;
import com.example.tttndemo.entity.*;
import com.example.tttndemo.exception.OrderNotFoundException;
import com.example.tttndemo.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    public static final int ORDERS_PER_PAGE = 5;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final PromotionDetailRepository promotionDetailRepository;

    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, OrderStatusRepository orderStatusRepository, CartItemRepository cartItemRepository, AddressRepository addressRepository, UserRepository userRepository, PromotionDetailRepository promotionDetailRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.cartItemRepository = cartItemRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.promotionDetailRepository = promotionDetailRepository;
        this.productRepository = productRepository;
    }

    public Order getOrder(Integer orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId).get();
        if(order == null){
            throw new OrderNotFoundException("Couldn't find order with id: "+ orderId);
        }
        return order;
    }

    public List<Order> getOrderByUser(Integer userId){
        return orderRepository.findByUserId(userId);
    }

    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }

    public Order createOrder(Integer addressId, Integer userId, String paymentId){

        List<CartItem> listItem = cartItemRepository.findCartItemByUserId(userId);
        List<CartDTO> cartDTOList = new ArrayList<>();
        for(CartItem c : listItem){
            cartDTOList.add(new CartDTO(c,
                    new ProductDTO(promotionDetailRepository.FindHighestPercentageForProduct(c.getProduct().getId()),c.getProduct())));
        }
        User user = userRepository.findById(userId).get();
        Address address = addressRepository.findById(addressId).get();
        Order order = new Order();
        OrderStatus  orderStatus = new OrderStatus(1);
        double totalPrice = 0;
        for(CartDTO c : cartDTOList){
            totalPrice += c.getSubtotal();
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        totalPrice = Double.parseDouble(decimalFormat.format(totalPrice));

        order.setTotalPrice(totalPrice);
        order.setOrderStatus(orderStatus);
        order.setDate(new Date());
        order.setUser(user);
        order.setAddress(address);
        order.setPaymentId(paymentId);
        Order savedOrder = orderRepository.save(order);
        for(CartDTO c : cartDTOList){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(productRepository.findById(c.getProduct().getId()).get());
            orderDetail.setQuantity(c.getQuantity());
            orderDetail.setUnitPrice(c.getProduct().getPriceDiscount());
            orderDetail.setOrder(savedOrder);
            orderDetailRepository.save(orderDetail);
            Product p = productRepository.findById(c.getProduct().getId()).get();
            p.setInStock(p.getInStock() - c.getQuantity());
            p.setSoldQuantity(p.getSoldQuantity() + c.getQuantity());
            productRepository.save(p);
        }
        return savedOrder;


    }

    public List<Order> getAllOrder(User user, Integer orderStatusId) {
        if(orderStatusId == null){
            return orderRepository.findByUserId(user.getId());
        }
        return orderRepository.findByOrderStatus(user.getId(),orderStatusId);

    }


    public Page<Order> listByPage(int pageNum,  String status) {
        Sort sort = Sort.by("id");
        sort = sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);
        if(!status.equals("ALL")){
            Integer orderStatus = Integer.parseInt(status);
            return orderRepository.findAdminByStatus(orderStatus,pageable);
        }
        return orderRepository.findAll(pageable);
    }

    public void acceptOrder(Integer id) throws OrderNotFoundException {
        try {
            Order order = orderRepository.findByOrderId(id);
            Integer statusId = order.getOrderStatus().getId();
            switch (statusId) {
                case 1: { //Neu dang cho xac nhan thi -> cho giao hang
                    order.setOrderStatus(orderStatusRepository.getOrderStatusById(2));
                    break;
                }
                case 2: { //Neu dang cho giao hang -> dang giao hang
                    order.setOrderStatus(orderStatusRepository.getOrderStatusById(3));
                    break;
                }
                case 3: { //Neu dang giao thi -> cho da hang
                    order.setOrderStatus(orderStatusRepository.getOrderStatusById(4));
                    break;
                }
                case 5: { //Neu dang cho yeu cau huy thi -> Da huy
                    List<OrderDetail> orderDetail = orderRepository.getOrderDetail(id);

                    for(OrderDetail item : orderDetail)
                    {
                        Product product = item.getProduct();
                        product.setSoldQuantity(product.getSoldQuantity() - item.getQuantity());
                        product.setInStock(product.getInStock() + item.getQuantity());
                        productRepository.save(product);
                    }
                    order.setOrderStatus(orderStatusRepository.getOrderStatusById(6));
                    break;
                }
                default:
                    break;
            }
            orderRepository.save(order);
        }
        catch(NoSuchElementException ex) {
            throw new OrderNotFoundException("Could not find any order with ID " + id);

        }
    }

    public void denyOrder(Integer id) throws OrderNotFoundException {
        try {
            Order order = orderRepository.findByOrderId(id);
            Integer statusId = order.getOrderStatus().getId();

            switch (statusId) {
                case 1:
                case 2: { //Neu dang cho xac nhan, cho giao -> da huy
                    List<OrderDetail> orderDetail = orderRepository.getOrderDetail(id);

                    for(OrderDetail item : orderDetail)
                    {
                        Product product = item.getProduct();
                        product.setInStock(product.getInStock() + item.getQuantity());
                        product.setSoldQuantity(product.getSoldQuantity() - item.getQuantity());
                        productRepository.save(product);
                    }

                    order.setOrderStatus(orderStatusRepository.getOrderStatusById(5));

                    break;
                }
                case 5: { //Neu dang yeu cau huy thi -> cho xac nhan
                    order.setOrderStatus(orderStatusRepository.getOrderStatusById(1));
                    break;
                }
                default:
                    break;
            }
            orderRepository.save(order);
        }
        catch(NoSuchElementException ex) {
            throw new OrderNotFoundException("Could not find any order with ID " + id);

        }
    }


}