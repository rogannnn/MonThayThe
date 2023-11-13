package com.example.tttndemo.dto;


import com.example.tttndemo.entity.CartItem;
import com.example.tttndemo.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    Integer id;
    private ProductDTO product;
    private int quantity;


    public CartDTO(CartItem cartItem, ProductDTO product) {
        this.id = cartItem.getId();
        this.quantity = cartItem.getQuantity();
        this.product = product;
    }

    public float getSubtotal(){
        return (float)(this.product.getPriceDiscount() * quantity);
    }
}
