package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.ReviewDto;
import com.example.mon_thay_the.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListReviewResponse  extends BaseResponse{
    private List<ReviewDto> data;
//    private Integer totalPage;
//    private Integer pageNum;

    public ListReviewResponse(Integer result, String msg, String method, Integer code, List<ReviewDto> data){
        super(result, msg, method, code);
//        this.totalPage = totalPage;
//        this.pageNum = pageNum;
        this.data = data;
    }
}
