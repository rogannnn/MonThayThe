package com.example.mon_thay_the.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a well-formed email address")
    private String email;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 64, message = "First name should be less than 64 characters")
    private String firstName;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 45, message = "Last name should be less than 45 characters")
    private String lastName;


    @NotBlank(message = "Password must not be blank")
    @Size(min = 6 , message = "Password should be greater than equal to 6 characters")
    @Size(max = 64, message = "Password should be less than 64 characters")
    private String password;
}
