package com.example.tttndemo.controller;


import com.example.tttndemo.entity.Role;
import com.example.tttndemo.entity.User;
import com.example.tttndemo.exception.UserNotFoundException;
import com.example.tttndemo.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/register")
    public String newUser(Model model) {
        model.addAttribute("pageTitle", "Create New User");
        return "register";
    }


    @PostMapping("/user/save")
    @ResponseBody
    public String registerUser(@RequestParam("email") String email,
                               @RequestParam("firstName") String firstName,
                               @RequestParam("lastName") String lastName,
                               @RequestParam("password") String password) {

        if(firstName == null || lastName == null || password == null){
            return "register new user fail";
        }
        if(userService.registerNewUser(email,firstName, lastName, password)){
            return "register new user successfully";
        }
        return "successfully";

    }

    @PostMapping("/user/new")
    @ResponseBody
    public String saveNewUser( User user, Model model) {
        user.setRole(new Role(2));
        user.setRegistrationDate(new Date());
        user.setEnabled(true);
        userService.saveUser(user);;
        return "create new user success";
    }

    @GetMapping("/users/check_email")
    @ResponseBody
    public String checkEmailIsUnique(@RequestParam("email") String email) {
        User user = null;
        String result = null;
        try {
             user = userService.getUserByEmail(email);
        }catch (UserNotFoundException e){
            result = "OK";
        }
        if(user != null){
            result = "Duplicated";
        }
        return result;
    }


    @GetMapping("/admin/user")
    public String adminFirstPageUser() {
        return "redirect:/admin/user/page/1";
    }

    @GetMapping("/admin/user/page/{pageNum}")
    public String adminPageUser(@PathVariable(name = "pageNum") Integer pageNum, Model model,
                                @RequestParam(defaultValue = "") String keyword,
                                @RequestParam(defaultValue = "id") String sortField,
                                @RequestParam(required = false) String sortDir) {

        model.addAttribute("sortField", sortField);

        if(sortDir == null) sortDir = "asc";
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);

        model.addAttribute("keyword", keyword);


        Page<User> page = userService.listByPage(pageNum, keyword, sortField, sortDir);
        List<User> listUsers = page.getContent();
        long startCount = (pageNum - 1) * userService.USER_PER_PAGE + 1;
        long endCount = startCount +  userService.USER_PER_PAGE - 1;

        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }



        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("listUsers", listUsers);


        return "user/users";
    }

    @GetMapping("/admin/user/add")
    public String addUser(Model model) {
        User user = userService.createNewUser();
        user.setEnabled(true);
        List<Role> listRoles = userService.listRoles();

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", user);
        }
        model.addAttribute("listRoles", listRoles);

        return "user/new_user";
    }

    @PostMapping("/admin/user/add")
    public String saveUser(User user, BindingResult errors, RedirectAttributes redirectAttributes) {

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setRegistrationDate(new Date());
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("messageSuccess", "The user has been saved successfully.");
        return "redirect:/admin/user/page/1";

    }

    @GetMapping("/admin/user/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("messageSuccess", "The user ID " + id + " has been deleted successfully");
        }
        catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/admin/user/page/1";
    }


    @GetMapping("/admin/user/edit/{id}")
    public String editUser(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes, Model model) {
        try {
            List<Role> listRoles = userService.listRoles();
            model.addAttribute("listRoles", listRoles);
            model.addAttribute("isEdit", true);


            if (!model.containsAttribute("user")) {
                User user = userService.getUserByID(id);
                model.addAttribute("user", user);
            }
            return "user/new_user";
        }
        catch ( UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/admin/user/page/1";

        }
    }

    @PostMapping("/admin/user/edit/{id}")
    public String saveEditUser(User user, RedirectAttributes redirectAttributes,
                               @PathVariable("id") Integer id) throws UserNotFoundException {

            User existUser = userService.getUserByID(id);
            user.setPassword(existUser.getPassword());
            user.setRegistrationDate(existUser.getRegistrationDate());

            userService.saveUser(user);

            redirectAttributes.addFlashAttribute("messageSuccess", "The user has been edited successfully.");
            return "redirect:/admin/user/page/1";

    }
}
