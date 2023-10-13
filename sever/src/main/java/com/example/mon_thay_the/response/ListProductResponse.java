package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.ProductDto;
import com.example.mon_thay_the.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListProductResponse extends BaseResponse {

    private List<ProductDto> data;
    private Integer totalPage;
    private Integer pageNum;

    public ListProductResponse(Integer result, String msg, String method, Integer code, List<ProductDto> data, Integer totalPage, Integer pageNum){
        super(result, msg, method, code);
        this.data = data;
        this.totalPage = totalPage;
        this.pageNum = pageNum;
    }
}
