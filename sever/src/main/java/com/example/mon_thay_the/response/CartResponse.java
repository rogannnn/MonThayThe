package com.example.mon_thay_the.response;


import com.example.mon_thay_the.dto.CartItemDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartResponse extends  BaseResponse{
    private CartItemDto data;

    public CartResponse(Integer result, String msg, String method, Integer code, CartItemDto data){
        super(result, msg, method, code);
        this.data = data;
    }
}
