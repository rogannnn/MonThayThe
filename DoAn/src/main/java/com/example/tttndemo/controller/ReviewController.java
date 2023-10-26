package com.example.tttndemo.controller;

import com.example.tttndemo.authentication.MyUserDetails;
import com.example.tttndemo.entity.Review;
import com.example.tttndemo.service.ReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review/new")
    @ResponseBody
    public String addNewReivew(@RequestParam("productId") Integer productId,
                               @RequestParam("comment") String comment,
                               @RequestParam("vote") Integer vote,
                               @AuthenticationPrincipal MyUserDetails myUserDetails){
        if(myUserDetails != null){
            Review review = reviewService.newReview(myUserDetails.getId(),productId,comment, vote);
            return "add new review success";
        }
        return "add new review fail";
    }
}
