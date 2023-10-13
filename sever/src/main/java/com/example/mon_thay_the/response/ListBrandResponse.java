package com.example.mon_thay_the.response;


import com.example.mon_thay_the.dto.BrandDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ListBrandResponse extends BaseResponse {
    List<BrandDto> data;


    public ListBrandResponse(Integer result, String msg, String method, Integer code, List<BrandDto> data){
        super(result, msg, method, code);
        this.data = data;
    }
}
