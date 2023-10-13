package com.example.mon_thay_the.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckoutRequest {

    @NotBlank(message = "Address id must not be blank")
    private Integer addressId;
}
