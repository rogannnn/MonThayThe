package com.example.mon_thay_the.dto;

import com.example.mon_thay_the.entity.Product;
import com.example.mon_thay_the.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDto {

    private LocalDateTime date;
    private String userName;
    private String comment;
    private Integer vote;

    public ReviewDto(Review review){
        this.comment = review.getComment();
        this.vote = review.getVote();
        this.date = review.getDate();
        this.userName = review.getUser().getFullName();
    }



}
