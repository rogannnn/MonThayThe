package com.example.mon_thay_the.controller;

import com.example.mon_thay_the.dto.ReviewDto;
import com.example.mon_thay_the.entity.Review;
import com.example.mon_thay_the.entity.User;
import com.example.mon_thay_the.exception.ProductNotFoundException;
import com.example.mon_thay_the.principal.MyUserDetails;
import com.example.mon_thay_the.request.ReviewRequest;
import com.example.mon_thay_the.response.BaseResponse;
import com.example.mon_thay_the.response.ListReviewResponse;
import com.example.mon_thay_the.service.ProductService;
import com.example.mon_thay_the.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ReviewController {


    private final ReviewService reviewService;

    private final ProductService productService;

    @GetMapping("/api/review/{id}")
    public ResponseEntity<ListReviewResponse> getListReviewByProductId(@PathVariable("id") Integer productId,
                                                                       @RequestParam(value = "pageNum",required = false)Optional<Integer> ppageNum,
                                                                       HttpServletRequest request){

        int pageNum = 1;
        if(ppageNum.isPresent()){
          pageNum = ppageNum.get();
        }
//        Page<Review> reviewListPage = reviewService.listReviewPerPage(productId, pageNum);
        List<Review> reviewList = reviewService.getListReviewByProductId(productId);
        System.out.println(reviewList.size());

        List<ReviewDto> reviewDtoList = new ArrayList<>();

        for(Review r : reviewList){
            reviewDtoList.add(new ReviewDto(r));
        }

        ListReviewResponse data = new ListReviewResponse(1,"Get list review successfully1",
                request.getMethod(), HttpStatus.OK.value(),reviewDtoList);

        return  new ResponseEntity(data, HttpStatus.OK);

    }

    @PostMapping("/api/review/new")
    public ResponseEntity<BaseResponse> addNewReview(HttpServletRequest request,
                                                     @Valid @RequestBody ReviewRequest reviewRequest,
                                                     @AuthenticationPrincipal MyUserDetails userDetails) throws ProductNotFoundException {

        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setVote(reviewRequest.getVote());
        review.setProduct(productService.getProductById(reviewRequest.getProductId()));
        review.setDate( LocalDateTime.now());
        review.setUser(userDetails.getUser());

        reviewService.saveReview(review);

        BaseResponse data = new BaseResponse(1,"Add new review successfully!", request.getMethod(), HttpStatus.CREATED.value());
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }
}
