package com.example.mon_thay_the.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReviewRequest {
    @NotNull(message = "Product id must not be null")
    private Integer productId;

    @NotNull(message = "Vote must not be null")
    @Min(value = 0, message = "Vote should be greater than equal 0")
    @Max(value = 5, message = "Vote should be less than equal 5")
    private Integer vote;

    @NotBlank(message = "Comment must not be blank")
    private String comment;
}
