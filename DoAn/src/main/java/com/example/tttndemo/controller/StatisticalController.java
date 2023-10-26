package com.example.tttndemo.controller;

import com.example.tttndemo.entity.Order;
import com.example.tttndemo.exception.OrderNotFoundException;
import com.example.tttndemo.export.OrderDetailExport;
import com.example.tttndemo.export.TopSellingProductExport;
import com.example.tttndemo.service.StatisticalService;
import com.example.tttndemo.utils.TopSellingProduct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class StatisticalController {

    private final StatisticalService statisticalService;

    public StatisticalController(StatisticalService statisticalService) {
        this.statisticalService = statisticalService;
    }

    @GetMapping("/admin/statistical")
    public String getStatisticalPage(){
        return "statistical/statistical";
    }

    @PostMapping("/admin/statistical/topsellproduct")
    @ResponseBody
    public void exportToPdf(@RequestParam("limit") Integer limit,
                            @RequestParam("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
                            @RequestParam("finishDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date finishDate,
                            HttpServletResponse response) throws  IOException {

        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "inline; filename=top_=" + limit + "_selling_product.pdf";
        response.setHeader(headerKey, headerValue);


        List<TopSellingProduct> productList = statisticalService.getTopSellingProducts(startDate, finishDate, limit);


        TopSellingProductExport exporter = new TopSellingProductExport(productList);
        exporter.export(response, limit, startDate, finishDate);
    }

}
