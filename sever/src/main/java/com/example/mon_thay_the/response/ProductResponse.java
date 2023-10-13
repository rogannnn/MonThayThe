package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.ProductDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse extends BaseResponse{
    private ProductDto data;

    public ProductResponse(Integer result, String msg, String method, Integer code, ProductDto data){
        super(result, msg, method, code);
        this.data = data;
    }
}
