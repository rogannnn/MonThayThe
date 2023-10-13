package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.OrderDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse extends BaseResponse {
    private OrderDto data;

    public OrderResponse(Integer result, String msg, String method, Integer code, OrderDto data){
        super(result, msg, method, code);
        this.data = data;
    }
}
