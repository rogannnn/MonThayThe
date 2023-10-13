package com.example.mon_thay_the.response;


import com.example.mon_thay_the.dto.CategoryDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse extends BaseResponse {

    private CategoryDto data;

    public CategoryResponse(Integer result, String msg, String method, Integer code, CategoryDto data){
        super(result, msg, method, code);
        this.data = data;
    }
}
