package com.example.tttndemo.controller;


import com.example.tttndemo.service.ReportService;
import com.example.tttndemo.utils.Items;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class AdminController {


    private final ReportService reportService;

    public AdminController(ReportService reportService) {
        this.reportService = reportService;
    }


    @GetMapping("/admin")
    public String adminDashboard(Model model) throws ParseException {


        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        List<Items> listEarn = reportService.reportEarn();
        long countUserByWeek = reportService.countUserByWeek(currentDate);
        long countOrderByWeek = reportService.countOrderByWeek(currentDate);
        long countReviewByWeek = reportService.countReviewByWeek(currentDate);
        long totalOrderByWeek = reportService.countTotalOrderByWeek(currentDate);


        List<String> listEarnKey = new ArrayList<>();
        List<Long> listEarnValue =  new ArrayList<>();

        for(Items item : listEarn) {
            listEarnKey.add(item.getName());
            listEarnValue.add(item.getValue());
        }

        model.addAttribute("listEarnKey", listEarnKey);
        model.addAttribute("listEarnValue", listEarnValue);
        model.addAttribute("countUserByWeek", countUserByWeek);
        model.addAttribute("countOrderByWeek", countOrderByWeek);
        model.addAttribute("countReviewByWeek", countReviewByWeek);
        model.addAttribute("totalOrderByWeek", totalOrderByWeek);

        return "admin";
    }

}
