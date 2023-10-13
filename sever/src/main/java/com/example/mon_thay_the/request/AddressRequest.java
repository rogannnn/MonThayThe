package com.example.mon_thay_the.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name should be less than 255 characters")
    private String name;

    @NotBlank(message = "Specific address must not be blank")
    private String specificAddress;

    @NotBlank(message = "Phone must not be blank")
    @Size(max = 11, message = "Phone should be less than 11 characters")
    private String phone;

    @NotBlank(message = "Ward code must not be blank")
    private String wardCode;

    private Boolean isDefault;

}
