package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.PromotionDto;
import com.example.mon_thay_the.entity.Promotion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListPromotionResponse extends BaseResponse{

    private List<PromotionDto> data;

    public ListPromotionResponse(Integer result, String msg, String method, Integer code,  List<PromotionDto>  data){
        super(result, msg, method, code);
        this.data = data;
    }
}
