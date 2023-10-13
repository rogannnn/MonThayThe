package com.example.mon_thay_the.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ForgotPasswordRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a well-formed email address")
    private String email;

}
