package com.example.mon_thay_the.response;


import com.example.mon_thay_the.dto.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListAddressResponse  extends BaseResponse{

    List<AddressDto> data;

    public ListAddressResponse(Integer result, String msg, String method, Integer code, List<AddressDto> data) {
        super(result, msg, method, code);
        this.data = data;
    }
}
