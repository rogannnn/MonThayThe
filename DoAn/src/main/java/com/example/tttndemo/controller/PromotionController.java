package com.example.tttndemo.controller;

import com.example.tttndemo.entity.Product;
import com.example.tttndemo.entity.Promotion;
import com.example.tttndemo.exception.PromotionNotFoundException;
import com.example.tttndemo.service.ProductService;
import com.example.tttndemo.service.PromotionService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class PromotionController {

    private final PromotionService promotionService;
    private final ProductService productService;


    public PromotionController(PromotionService promotionService, ProductService productService) {
        this.promotionService = promotionService;
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
        model.addAttribute("promotion", promotion);

        return "promotion/new_promotion";
    }
    @PostMapping("admin/promotion/add")
    public String savePromotion(Promotion promotion,
                                @RequestParam("start-date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
                                @RequestParam("finish-date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date finishDate
                                ,RedirectAttributes redirectAttributes)  {
//        System.out.println(startDate);
//        System.out.println(finishDate);
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        promotion.setStartDate(startDate);
        promotion.setFinishDate(finishDate);
        System.out.println(startDate);
        System.out.println(finishDate);

        promotionService.savePromotion(promotion);
        redirectAttributes.addFlashAttribute("messageSuccess", "The promotion has been saved successfully.");
        return "redirect:/admin/promotion";

    }

    @GetMapping("/admin/promotion/edit/{id}")
    public String editPromotion(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        Promotion promotion = promotionService.getPromotionById(id);

        model.addAttribute("promotion", promotion);
        return "promotion/new_promotion";
    }

    @PostMapping("/admin/promotion/edit/{id}")
    public String saveEditPromotion(Promotion promotion, RedirectAttributes redirectAttributes,
                                   @PathVariable("id") Integer id) throws IOException {

        promotionService.savePromotion(promotion);

        redirectAttributes.addFlashAttribute("messageSuccess", "The promotion has been edited successfully.");
        return "redirect:/admin/promotion";




    }

    @GetMapping("/admin/promotion/delete/{id}")
    public String deletePromotion(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            promotionService.deletePromotion(id);
            redirectAttributes.addFlashAttribute("messageSuccess", "The Promotion ID " + id + " has been deleted successfully");
        }
        catch (PromotionNotFoundException ex) {
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/admin/promotion/page/1";
    }
}
