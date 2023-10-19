package com.example.mon_thay_the.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.mon_thay_the.error.Error.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = EMAIL_BLANK_ERROR)
    @Email(message = EMAIL_VALIDATION)
    private String email;

    @NotBlank(message = FIRSTNAME_BLANK_ERROR)
    private String firstName;

    @NotBlank(message = LASTNAME_BLANK_ERROR)
    private String lastName;


    @NotBlank(message = PASSWORD_BLANK_ERROR)
//    @Size(min = 6 , message = "Password should be greater than equal to 6 characters")
//    @Size(max = 64, message = "Password should be less than 64 characters")
    private String password;
}
