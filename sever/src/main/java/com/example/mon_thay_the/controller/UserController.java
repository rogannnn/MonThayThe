package com.example.mon_thay_the.controller;

import com.example.mon_thay_the.dto.UserDto;
import com.example.mon_thay_the.entity.User;
import com.example.mon_thay_the.principal.MyUserDetails;
import com.example.mon_thay_the.request.ChangeNameRequest;
import com.example.mon_thay_the.request.ChangePasswordRequest;
import com.example.mon_thay_the.response.BaseResponse;
import com.example.mon_thay_the.response.UserResponse;
import com.example.mon_thay_the.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/info")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal MyUserDetails userDetails,
            HttpServletRequest request){
        User user = userDetails.getUser();
        UserDto userDto = new UserDto(user);

        UserResponse data = new UserResponse(1,"Get info user successfully!",request.getMethod(), HttpStatus.OK.value(), userDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping("/change-name")
    public ResponseEntity<UserResponse> changeNameUser(@AuthenticationPrincipal MyUserDetails userDetails
            , HttpServletRequest request
            , @Valid @RequestBody ChangeNameRequest changeNameRequest)
    {
        User user = userDetails.getUser();

        user.setFirstName(changeNameRequest.getFirstName());
        user.setLastName(changeNameRequest.getLastName());

        User savedUser = userService.save(user);
        UserDto userDto = new UserDto(savedUser);
        UserResponse data = new UserResponse(1,"Change user name successfully!", request.getMethod(), HttpStatus.OK.value(), userDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @PutMapping("/change-password")
    public ResponseEntity<BaseResponse> changePassword(@AuthenticationPrincipal MyUserDetails userDetails
            , HttpServletRequest request
            , @Valid @RequestBody ChangePasswordRequest changePasswordRequest){

            User user =userDetails.getUser();

            if(BCrypt.checkpw(changePasswordRequest.getOldPassword(), user.getPassword())){
                if(changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())){
                    user.setPassword(changePasswordRequest.getNewPassword());
                    userService.saveUser(user);
                    BaseResponse data = new BaseResponse(1,"Change password successfully!"
                            , request.getMethod(),HttpStatus.OK.value());
                    return new ResponseEntity<>(data, HttpStatus.OK);
                }else {
                    BaseResponse baseResponse = new BaseResponse(0,"New password and confirm password do not match"
                            , request.getMethod(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
                }
            }
            BaseResponse response = new BaseResponse(0,"Old password not correct!"
                    , request.getMethod(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

}
