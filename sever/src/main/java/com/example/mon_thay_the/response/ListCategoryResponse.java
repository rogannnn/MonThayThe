package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.CategoryDto;
import com.example.mon_thay_the.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListCategoryResponse extends BaseResponse{

    private List<CategoryDto> data;


    public ListCategoryResponse(Integer result, String msg, String method, Integer code, List<CategoryDto> data){
        super(result, msg, method, code);
        this.data = data;
    }
}
