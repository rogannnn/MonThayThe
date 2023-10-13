package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.CategoryDto;
import com.example.mon_thay_the.dto.DistrictDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListDistrictResponse extends BaseResponse{

    private List<DistrictDto> data;

    public ListDistrictResponse(Integer result, String msg, String method, Integer code, List<DistrictDto> data){
        super(result, msg, method, code);
        this.data = data;
    }
}
