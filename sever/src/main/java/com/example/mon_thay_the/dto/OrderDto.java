package com.example.mon_thay_the.dto;


import com.example.mon_thay_the.entity.Order;
import com.example.mon_thay_the.entity.OrderDetail;
import com.example.mon_thay_the.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDto {

    private Integer id;
    private Date date;
    private AddressDto address;
    private Double totalPrice;
    private OrderStatus orderStatus;
    private List<OrderDetailDto> list = new ArrayList<>();

    public OrderDto(Order order){
        this.id = order.getId();
        this.date = order.getDate();
        this.address = new AddressDto(order.getAddress());
        this.orderStatus = order.getOrderStatus();
        this.totalPrice = order.getTotalPrice();

        for(OrderDetail o : order.getOrderDetails()){
            this.list.add(new OrderDetailDto(o));
        }
    }


    public OrderDto(Order order, List<OrderDetailDto> orderDetailDtoList){
        this.id = order.getId();
        this.date = order.getDate();
        this.address = new AddressDto(order.getAddress());
        this.totalPrice = order.getTotalPrice();
        this.orderStatus = order.getOrderStatus();

        this.list = orderDetailDtoList;
    }

}
