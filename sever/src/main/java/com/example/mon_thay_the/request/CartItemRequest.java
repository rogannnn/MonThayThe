package com.example.mon_thay_the.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartItemRequest {

    @NotNull(message = "Product id must not be null!")
    private Integer id;


    @NotNull(message = "Quantity must not be null!")
    @Min(value = 1, message = "Quantity must be positive!")
    private Integer quantity;
}
