package com.example.tttndemo.controller;

import com.example.tttndemo.entity.User;
import com.example.tttndemo.exception.UserNotFoundException;
import com.example.tttndemo.service.EmailService;
import com.example.tttndemo.service.OtpService;
import com.example.tttndemo.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

@Controller
public class AuthenController {

    private final UserService userService;
    private final OtpService otpService;

    private final EmailService emailService;

    public AuthenController(UserService userService, OtpService otpService, EmailService emailService) {
        this.userService = userService;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @ResponseBody
    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(@RequestParam("id") Integer id, @RequestParam("email") String email){
        return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }

    @GetMapping("/forgot-password/email")
    public String getEmail() {
        return "password/email";
    }

    @PostMapping("/forgot-password/generateOTP")
    @ResponseBody
    public String generateOTP(@RequestParam("email") String email, HttpSession session){
        try {
            session.setAttribute("email",email);
            User user = userService.getUserByEmail(email);
            int otp =  otpService.generateOTP(user.getEmail());
            emailService.sendEmailOTP(user,otp);
            return "success";
        } catch (UserNotFoundException e) {
            return "not found";
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/forgot-password/otp")
    public String getOtp(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        model.addAttribute("email",email);
        return "password/otp";
    }

    @PostMapping("/forgot-password/validateOTP")
    @ResponseBody
    public String validateOTP(@RequestParam("otp") Integer otp, HttpSession session){
        String email = (String) session.getAttribute("email");
        System.out.println(otp);
        try {
            User user = userService.getUserByEmail(email);
            if (otpService.isOtpValid(otp, user.getEmail())){
                return "The OTP code are incorrect";
            }
        } catch (UserNotFoundException ex){
            System.out.println("User not found with email: " + email);
            return "not found";
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        session.setAttribute("otpValid",true);
        return "success";
    }

    @GetMapping("/forgot-password/change_password")
    public String changePassword(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        model.addAttribute("email",email);
        return "password/change_password";
    }

    @PostMapping("/forgot-password/handle")
    @ResponseBody
    public String progressChangePass(@RequestParam("newPassword") String newPassword,
                                     HttpSession session) {
        String email = (String) session.getAttribute("email");
        try {
            User user = userService.getUserByEmail(email);
            Boolean otpValid = (Boolean) session.getAttribute("otpValid");
            if(otpValid){
                user.setPassword(newPassword);
                userService.saveUser(user);
                otpService.clearOTP(user.getEmail());
                return "change password success";
            }else return "change password fail";
        }catch (UserNotFoundException ex){
            return "you have to complete all the steps to do it";
        }
    }


}
