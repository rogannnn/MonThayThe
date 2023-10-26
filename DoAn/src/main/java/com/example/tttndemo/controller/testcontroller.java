package com.example.tttndemo.controller;

import com.example.tttndemo.entity.Promotion;
import com.example.tttndemo.entity.User;
import com.example.tttndemo.exception.UserNotFoundException;
import com.example.tttndemo.service.OrderService;
import com.example.tttndemo.service.PromotionService;
import com.example.tttndemo.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class testcontroller {

    private final PromotionService promotionService;
    private final OrderService orderService;

    private final UserService userService;

    public testcontroller(PromotionService promotionService, OrderService orderService, UserService userService) {
        this.promotionService = promotionService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/test")
    public String test1() throws UserNotFoundException {
//        orderService.deleteOrder(35);
        User user = userService.getUserByID(3);
        user.setPassword("123456");
        userService.saveUser(user);
        return "test";
    }

    @PostMapping("/test")
    public String tes1(Model model,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                       @RequestParam(name = "finishDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishDate
    ){
        System.out.println(startDate);
        System.out.println(startDate.getClass());
        System.out.println(finishDate);
        model.addAttribute("result", finishDate);
        Promotion promotion = new Promotion(startDate,finishDate);
        promotionService.savePromotion(promotion);
        return "test";

    }
}
