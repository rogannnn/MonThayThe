package com.example.mon_thay_the.dto;


import com.example.mon_thay_the.entity.CartItem;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemDto {
    private Integer id;
    private ProductDto product;
    private Integer quantity;

    public CartItemDto(CartItem cartItem){
        this.product = new ProductDto(cartItem.getProduct());
        this.id = cartItem.getId();
        this.quantity = cartItem.getQuantity();
    }
}
