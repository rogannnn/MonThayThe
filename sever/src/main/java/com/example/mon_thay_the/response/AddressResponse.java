package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.AddressDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse extends BaseResponse{
    private AddressDto data;

    public AddressResponse(Integer result, String msg, String method, Integer code, AddressDto data){
        super(result, msg, method, code);
        this.data = data;
    }
}
