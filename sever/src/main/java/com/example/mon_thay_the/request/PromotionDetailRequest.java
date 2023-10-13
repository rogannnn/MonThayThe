package com.example.mon_thay_the.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PromotionDetailRequest {

    @NotNull(message = "Percentage must not be null")
    @Min(value = 0, message = "Percentage should be greater than 0")
    @Max(value = 100, message = "Percentage should be less than 100")
    private Integer percentage;

    @NotNull(message = "Product must not be null!")
    private String productId;
}
