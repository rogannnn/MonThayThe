package com.example.mon_thay_the.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PromotionRequest {

    @NotBlank(message = "Id must not be blank!")
    @Size(max = 10, message = "Id should be less than 10 characters")
    private String id;

    @NotBlank(message = "Name must not be blank!")
    @Size(max = 255, message = "Id should be less than 255 characters")
    private String name;

    @NotBlank(message = "Start Date must not be null!")
    private String startDate;

    @NotBlank(message = "End Date must not be null!")
    private String endDate;

    @NotNull(message = "Promotion detail must not be null")
    private List<PromotionDetailRequest> promotionDetails;


}
