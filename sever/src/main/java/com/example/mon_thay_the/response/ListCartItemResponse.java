package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.CartItemDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ListCartItemResponse extends BaseResponse{

    private List<CartItemDto> data;

    private Double total;

    public ListCartItemResponse(Integer result, String msg, String method, Integer code, List<CartItemDto> data, double total){
        super(result,msg, method, code);
        this.data = data;
        this.total = total;
    }
}
