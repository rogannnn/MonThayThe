package com.example.tttndemo.controller;

import com.example.tttndemo.entity.Brand;
import com.example.tttndemo.exception.BrandNotFoundException;
import com.example.tttndemo.service.BrandService;
import com.example.tttndemo.service.ProductService;
import com.example.tttndemo.service.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class BrandController {


    private final StorageService storageService;

    private final BrandService brandService;

    private final ProductService productService;

    public BrandController(StorageService storageService, BrandService brandService, ProductService productService) {
        this.storageService = storageService;
        this.brandService = brandService;
        this.productService = productService;
    }

    @GetMapping("/admin/brand")
    public String listFirstPage() {
        return "redirect:/admin/brand/page/1";
    }

    @GetMapping("/admin/brand/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") Integer pageNum, Model model,
                             @RequestParam(defaultValue = "") String keyword,
                             @RequestParam(defaultValue = "id") String sortField,
                             @RequestParam(required = false) String sortDir) {

        model.addAttribute("sortField", sortField);

        if(sortDir == null) sortDir = "asc";
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);

        model.addAttribute("keyword", keyword);


        Page<Brand> page = brandService.listByPage(pageNum, keyword, sortField, sortDir);
        List<Brand> listBrands = page.getContent();
        long startCount = (pageNum - 1) * brandService.BRAND_PER_PAGE + 1;
        long endCount = startCount +  brandService.BRAND_PER_PAGE - 1;

        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }



        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);

        model.addAttribute("listBrands", listBrands);


        return "brand/brands";
    }

    @GetMapping("admin/brand/add")
    public String addBrand(Model model) {
        Brand brand = new Brand();
        model.addAttribute("brand", brand);

        return "brand/new_brand";
    }

    @PostMapping("admin/brand/add")
    public String saveBrand(Brand brand,  RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file) throws IOException {

        String url = storageService.upload(file);

        brand.setImages(url);
        brandService.saveBrand(brand);
        redirectAttributes.addFlashAttribute("messageSuccess", "The brand has been saved successfully.");
        return "redirect:/admin/brand";

    }

    @GetMapping("/admin/brand/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            Brand brand = brandService.getBrandById(id);

            model.addAttribute("brand", brand);
            return "brand/new_brand";
        }
        catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/admin/brand";

        }
    }

    @PostMapping("/admin/brand/edit/{id}")
    public String saveEditBrand(Brand brand, RedirectAttributes redirectAttributes,
                                @PathVariable("id") Integer id, @RequestParam("file") MultipartFile file) {

        try {
            Brand existBrand = brandService.getBrandById(id);

            Brand brandCheckUnique = brandService.findBrandByName(brand.getName());

            if(!existBrand.getImages().equals(brand.getImages())) {
                String url = storageService.upload(file);
                brand.setImages(url);
            }
            brandService.saveBrand(brand);

            redirectAttributes.addFlashAttribute("messageSuccess", "The brand has been edited successfully.");
            return "redirect:/admin/brand";


        } catch (BrandNotFoundException | IOException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/admin/brand";
        }
    }

    @GetMapping("/admin/brand/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {

        try {
            if(productService.countByBrand(id) > 0){
                redirectAttributes.addFlashAttribute("messageError", "Không thể xóa nhãn hiệu vì tồn tại sản phẩm có nhãn hiệu này");
            }else {
                brandService.deleteBrand(id);
                redirectAttributes.addFlashAttribute("messageSuccess", "The brand ID " + id + " has been deleted successfully");
            }
        }
        catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/admin/brand/page/1";
    }
}
