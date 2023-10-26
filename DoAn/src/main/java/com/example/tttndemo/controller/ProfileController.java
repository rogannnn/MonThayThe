package com.example.tttndemo.controller;

import com.example.tttndemo.entity.User;
import com.example.tttndemo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public ProfileController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        model.addAttribute("user", user);
        return "profile/profile" ;
    }

    @GetMapping("/change-info")
    public String changeUserInfo(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        model.addAttribute("user", user);
        return "profile/change-info";
    }

    @PostMapping("/change-info/save")
    public String changeUserInfoPost(User user){

        userService.save(user);
        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public String changeUserPassword(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        model.addAttribute("user", user);
        return "profile/change-password";
    }

    @PostMapping("/change-password/save")
    @ResponseBody
    public String changeUserInfoPassword(@RequestParam("oldPassword") String oldPassword,
                                         @RequestParam("newPassword") String newPassword){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        if(BCrypt.checkpw(oldPassword,user.getPassword())){

            user.setPassword(newPassword);
            userService.saveUser(user);
            return "change password success";
        }
        return "wrong password";
    }
}
