package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.ProvinceDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListProvinceReponse extends BaseResponse{

    private List<ProvinceDto> data;


    public ListProvinceReponse(Integer result, String msg, String method, Integer code, List<ProvinceDto> data){
        super(result, msg, method, code);
        this.data = data;
    }
}
