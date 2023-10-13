package com.example.mon_thay_the.dto;


import com.example.mon_thay_the.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
    private Integer id;
    private Integer quantity;
    private Double unitPrice;

    private Boolean isReviewed;
    private ProductDto product;


    public OrderDetailDto(OrderDetail orderDetail){
        this.id =orderDetail.getId();
        this.quantity = orderDetail.getQuantity();
        this.unitPrice = orderDetail.getUnitPrice();
        this.product = new ProductDto(orderDetail.getProduct());
    }

}
