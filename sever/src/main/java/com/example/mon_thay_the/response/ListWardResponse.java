package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.WardDto;
import com.example.mon_thay_the.entity.Ward;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListWardResponse extends BaseResponse{

    private List<WardDto> data;


    public ListWardResponse(Integer result, String msg, String method, Integer code, List<WardDto> data){
        super(result, msg, method, code);
        this.data = data;
    }
}
