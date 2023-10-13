package com.example.mon_thay_the.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangeNameRequest {

    @NotBlank(message = "First name must not be blank")
    @Size(max = 64, message = "First name should be less than 64 characters")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Size(max = 45, message = "Last name should be less than 45 characters")
    private String lastName;

}
