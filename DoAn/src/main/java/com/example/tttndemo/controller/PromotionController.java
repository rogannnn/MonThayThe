package com.example.tttndemo.controller;

import com.example.tttndemo.entity.Product;
import com.example.tttndemo.entity.Promotion;
import com.example.tttndemo.entity.PromotionDetail;
import com.example.tttndemo.exception.PromotionNotFoundException;
import com.example.tttndemo.request.PromotionDetailRequest;
import com.example.tttndemo.request.PromotionRequest;
import com.example.tttndemo.service.ProductService;
import com.example.tttndemo.service.PromotionDetailService;
import com.example.tttndemo.service.PromotionService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PromotionController {

    private final PromotionService promotionService;

    private final PromotionDetailService promotionDetailService;
    private final ProductService productService;


    public PromotionController(PromotionService promotionService, PromotionDetailService promotionDetailService, ProductService productService) {
        this.promotionService = promotionService;
        this.promotionDetailService = promotionDetailService;
        this.productService = productService;
    }


    @GetMapping("/admin/promotion")
    public String listFirstPage() {
        return "redirect:/admin/promotion/page/1";
    }

    @GetMapping("/admin/promotion/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") Integer pageNum, Model model,
                             @RequestParam(defaultValue = "id") String sortField,
                             @RequestParam(required = false) String sortDir) {

        model.addAttribute("sortField", sortField);

        if(sortDir == null) sortDir = "asc";
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);


        Page<Promotion> page = promotionService.listByPage(pageNum, sortDir, sortField);
        List<Promotion> listPromotions = page.getContent();
        List<Product> listProduct = productService.getAll();
        long startCount = (pageNum - 1) * promotionService.PROMOTION_PER_PAGE + 1;
        long endCount = startCount +  promotionService.PROMOTION_PER_PAGE - 1;

        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }



        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);

        model.addAttribute("listPromotions", listPromotions);
        model.addAttribute("listProduct", listProduct);


        return "promotion/promotions";
    }

    @GetMapping("admin/promotion/add")
    public String addPromotion(Model model) {
        Promotion promotion = new Promotion();
        List<Product> productList = productService.getAll();
        model.addAttribute("promotion", promotion);
        model.addAttribute("productList", productList);

        return "promotion/new_promotion";
    }

    @PostMapping("admin/promotion/add")
    @ResponseBody
    public String savePromotion(@RequestBody PromotionRequest request) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Promotion promotion = new Promotion();
        promotion.setName(request.getName());
        promotion.setStartDate(dateFormat.parse(request.getStartDate()));
        promotion.setFinishDate(dateFormat.parse(request.getEndDate()));

        Promotion savedPromotion = promotionService.savePromotion(promotion);

        List<PromotionDetail> detailList = new ArrayList<>();
        for(PromotionDetailRequest detailRequest : request.getData()){

            PromotionDetail p = new PromotionDetail();
            p.setPercentage(detailRequest.getPercentage());
            p.setProduct(productService.getProductById(detailRequest.getProductId()));
            p.setPromotion(savedPromotion);
            detailList.add(p);
        }

        promotionDetailService.saveAll(detailList);
        if(savedPromotion != null){
            return "OK";
        }
        return "FAIL";
    }

    @GetMapping("admin/promotion/detail/{id}")
    public String viewPromotionDetail(@PathVariable("id") Integer id, Model model) {
        Promotion promotion = promotionService.getPromotionById(id);
        if(promotion == null) return "error/404";
        List<PromotionDetail> promotionDetails = (List<PromotionDetail>) promotion.getPromotionDetails();
        model.addAttribute("promotion", promotion);
        model.addAttribute("promotionDetails", promotionDetails);

        return "promotion/promotion_detail";
    }

    @PostMapping("/admin/promotion/check-before-save")
    @ResponseBody
    public String checkDuplicatePromotion( @RequestParam("start_date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
                                @RequestParam("finish_date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date finishDate,
                                           @RequestParam("name") String name) {

        List<Promotion> promotionList = promotionService.getAll();
        Boolean duplicateTime = false;
        Boolean duplicateName = false;
        for(Promotion p : promotionList){
            if(p.getStartDate().equals(startDate)  && p.getFinishDate().equals(finishDate)){
                duplicateTime = true;
            }

            if(p.getName().equals(name)){
                duplicateName = true;
            }
        }
        if(duplicateTime){
            return "Duplicate Time";
        }
        if(duplicateName){
            return "Duplicate Name";
        }
        return "OK";

    }
//    @GetMapping("/admin/promotion/edit/{id}")
//    public String editPromotion(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {
//        Promotion promotion = promotionService.getPromotionById(id);
//
//        model.addAttribute("promotion", promotion);
//        return "promotion/new_promotion";
//    }
//
//    @PostMapping("/admin/promotion/edit/{id}")
//    public String saveEditPromotion(Promotion promotion, RedirectAttributes redirectAttributes,
//                                   @PathVariable("id") Integer id) throws IOException {
//
//        promotionService.savePromotion(promotion);
//
//        redirectAttributes.addFlashAttribute("messageSuccess", "The promotion has been edited successfully.");
//        return "redirect:/admin/promotion";
//    }

    @GetMapping("/admin/promotion/delete/{id}")
    public String deletePromotion(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            promotionDetailService.deleteAll(id);
            promotionService.deletePromotion(id);

            redirectAttributes.addFlashAttribute("messageSuccess", "The Promotion ID " + id + " has been deleted successfully");
        }
        catch (PromotionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/admin/promotion/page/1";
    }
}
