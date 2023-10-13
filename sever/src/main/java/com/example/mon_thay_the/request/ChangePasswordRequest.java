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
public class ChangePasswordRequest {

    @NotBlank(message = "Please enter a old password in the field!")
    @Size(min = 6 , message = "Old password should be greater than equal to 6 characters")
    @Size(max = 64, message = "Old password should be less than 100 characters")
    private String oldPassword;

    @NotBlank(message = "Please enter a new password in the field!")
    @Size(min = 6 , message = "New password should be greater than equal to 6 characters")
    @Size(max = 64, message = "New password should be less than 64 characters")
    private String newPassword;

    @NotBlank(message = "Please enter a confirm password in the field!")
    @Size(min =6 , message = "Confirm password should be greater than equal to 6 characters")
    @Size(max = 64, message = "Confirm password should be less than 64 characters")
    private String confirmPassword;
}
