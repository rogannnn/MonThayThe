package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.OrderDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListOrderResponse extends BaseResponse {

    private List<OrderDto> data;


    public ListOrderResponse(Integer result, String msg, String method, Integer code, List<OrderDto> data){
        super(result, msg, method, code);
        this.data = data;
    }

}
