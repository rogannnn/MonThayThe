package com.example.mon_thay_the.controller;

import com.example.mon_thay_the.dto.OrderDto;
import com.example.mon_thay_the.entity.CartItem;
import com.example.mon_thay_the.entity.Order;
import com.example.mon_thay_the.entity.User;
import com.example.mon_thay_the.principal.MyUserDetails;
import com.example.mon_thay_the.response.BaseResponse;
import com.example.mon_thay_the.response.ListOrderResponse;
import com.example.mon_thay_the.response.OrderResponse;
import com.example.mon_thay_the.service.CartItemService;
import com.example.mon_thay_the.service.OrderService;
import com.example.mon_thay_the.service.OrderStatusService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderStatusService orderStatusService;
    private final CartItemService cartItemService;

    @GetMapping("/api/orders")
    public ResponseEntity<ListOrderResponse> getListOrder(HttpServletRequest request
            , @AuthenticationPrincipal MyUserDetails userDetails
            , @RequestParam(required = false, value = "type") Integer type){


        User user = userDetails.getUser();
        List<Order> orderList;
        if(type == null || type > 6){
            orderList = orderService.getAllOrder(user,null);
        }else orderList = orderService.getAllOrder(user,type);

        List<OrderDto> orderDtoList = new ArrayList<>();
        for(Order o: orderList){
            orderDtoList.add(new OrderDto(o));
        }

        ListOrderResponse data = new ListOrderResponse(1,"Get list order successfully!", request.getMethod(), HttpStatus.OK.value(), orderDtoList);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @GetMapping("/api/order/{id}")
    public ResponseEntity<OrderResponse> getOrderDetail(HttpServletRequest request,
            @PathVariable("id") Integer orderId,
            @AuthenticationPrincipal MyUserDetails userDetails){

            Order order = orderService.getOrderByIdAndUser(orderId, userDetails.getUser());

            OrderDto orderDto = new OrderDto(order);

            OrderResponse data = new OrderResponse(1,"Get oder detail successfully!",request.getMethod(), HttpStatus.OK.value(), orderDto);
            return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/api/order/purchase/{id}")
    public ResponseEntity<?> newOrder(HttpServletRequest request
            , @AuthenticationPrincipal MyUserDetails userDetails
            , @PathVariable("id") Integer addressId)
    {
        List<CartItem> itemList = cartItemService.listCartItems(userDetails.getUser());
        if( itemList.isEmpty()){
            BaseResponse baseResponse = new BaseResponse(0,"Cart is empty",request.getMethod(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }

        Order order =  orderService.createOrder(addressId, userDetails.getId());

        OrderDto orderDto = new OrderDto(order);
        OrderResponse data = new OrderResponse(1,"Order successfully!", request.getMethod(), HttpStatus.CREATED.value(), orderDto);
        return new ResponseEntity<>(data, HttpStatus.CREATED);

    }




}
