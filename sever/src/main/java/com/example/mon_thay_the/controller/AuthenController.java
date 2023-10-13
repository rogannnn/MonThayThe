package com.example.mon_thay_the.controller;

import com.example.mon_thay_the.dto.UserDto;
import com.example.mon_thay_the.entity.User;
import com.example.mon_thay_the.exception.DuplicateEmailException;
import com.example.mon_thay_the.exception.UserNotFoundException;
import com.example.mon_thay_the.principal.MyUserDetails;
import com.example.mon_thay_the.request.LoginRequest;
import com.example.mon_thay_the.request.SignUpRequest;
import com.example.mon_thay_the.response.SignUpReponse;
import com.example.mon_thay_the.service.AuthenService;
import com.example.mon_thay_the.service.JwtService;
import com.example.mon_thay_the.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
public class AuthenController {

    private final AuthenService authenService;

    private final JwtService jwtService;
    private final UserService userService;

    private final AuthenticationManager authenticationManager;


    @PostMapping("/api/signup")
    public ResponseEntity<SignUpReponse> signUp(@RequestBody SignUpRequest signUpRequest, HttpServletRequest request) throws DuplicateEmailException {
        User savedUser;
        if(userService.isEmailUnique(signUpRequest.getEmail())){
             savedUser = authenService.sigupUser(signUpRequest);
        }else throw new DuplicateEmailException("Email is being used!");

        MyUserDetails myUserDetails = new MyUserDetails(savedUser);
        String jwtToken = jwtService.generateToken(myUserDetails);
        String jwtRefeshToken = jwtService.generatereFreshToken(myUserDetails);
        UserDto user = new UserDto(savedUser);
        SignUpReponse data = new SignUpReponse(1,"Your registration was successful!",request.getMethod(), HttpStatus.CREATED.value(),user,jwtToken,jwtRefeshToken);
        return new ResponseEntity(data, HttpStatus.CREATED);
    }

    @PostMapping("/api/login")
    public  ResponseEntity<SignUpReponse> loginPage(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) throws UserNotFoundException {
       Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        User loginUser = userService.getUserByEmail(loginRequest.getEmail());
        if(loginUser == null) throw new UserNotFoundException("Not found account with the email: " + loginRequest.getEmail());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        MyUserDetails myUserDetails = new MyUserDetails(loginUser);
        String acessToken = jwtService.generateToken(myUserDetails);
        String refeshToken = jwtService.generatereFreshToken(myUserDetails);
        UserDto user = new UserDto(loginUser);
        SignUpReponse data = new SignUpReponse(1,"You has been logged successfully!",request.getMethod(), HttpStatus.OK.value(),user,acessToken,refeshToken);
        return new ResponseEntity(data, HttpStatus.CREATED);

    }

    @GetMapping("/success")
    public String success(){
        return "This is endpoint when you authenticated successfully!";
    }

}
