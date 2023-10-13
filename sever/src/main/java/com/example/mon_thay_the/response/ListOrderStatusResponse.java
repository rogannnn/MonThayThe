package com.example.mon_thay_the.response;

import com.example.mon_thay_the.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListOrderStatusResponse extends BaseResponse{

    private List<OrderStatus> data;


    public ListOrderStatusResponse(Integer result, String msg, String method, Integer code, List<OrderStatus> data){
        super(result, msg, method, code);
        this.data = data;
    }
}
