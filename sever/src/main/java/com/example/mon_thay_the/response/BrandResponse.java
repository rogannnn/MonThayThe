package com.example.mon_thay_the.response;


import com.example.mon_thay_the.dto.BrandDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandResponse extends BaseResponse{

    private BrandDto data;

    public BrandResponse(Integer result, String msg, String method, Integer code, BrandDto data){
        super(result, msg, method, code);
        this.data = data;
    }


}
