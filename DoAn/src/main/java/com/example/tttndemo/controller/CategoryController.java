package com.example.tttndemo.controller;

import com.example.tttndemo.entity.Category;
import com.example.tttndemo.exception.CategoryNotFoundException;
import com.example.tttndemo.service.CategoryService;
import com.example.tttndemo.service.ProductService;
import com.example.tttndemo.service.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {


    private final CategoryService categoryService;

    private final ProductService productService;


    private final StorageService storageService;

    public CategoryController(CategoryService categoryService, ProductService productService, StorageService storageService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.storageService = storageService;
    }


    @GetMapping("/admin/category")
    public String listFirstPage() {
        return "redirect:/admin/category/page/1";
    }

    @GetMapping("/admin/category/page/{pageNum}")
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


        Page<Category> page = categoryService.listByPage(pageNum, keyword, sortDir, sortField);
        List<Category> listCategories = page.getContent();
        long startCount = (pageNum - 1) * categoryService.CATEGORY_PER_PAGE + 1;
        long endCount = startCount +  categoryService.CATEGORY_PER_PAGE - 1;

        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }



        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);

        model.addAttribute("listCategories", listCategories);


        return "category/categories";
    }

    @GetMapping("admin/category/add")
    public String addCategory(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);

        return "category/new_category";
    }
    @PostMapping("admin/category/add")
    public String saveCategory(Category category, RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file) throws IOException {

        String url = storageService.upload(file);

        category.setImages(url);
        categoryService.saveCategory(category);
        redirectAttributes.addFlashAttribute("messageSuccess","Thêm loại sản phẩm thành công.");
        return "redirect:/admin/category";

    }

    @GetMapping("/admin/category/edit/{id}")
    public String editCategory(@PathVariable("id") Integer id, Model model) {
        Category category = categoryService.getCategoryById(id);

        model.addAttribute("category", category);
        return "category/new_category";
    }

    @PostMapping("/admin/category/edit/{id}")
    public String saveEditCategory(Category category, RedirectAttributes redirectAttributes,
                                   @PathVariable("id") Integer id, @RequestParam("file") MultipartFile file) throws IOException {

            Category existCategory = categoryService.getCategoryById(id);
            Category categoryCheckUnique = categoryService.getCategoryByName(category.getName());

            if(!existCategory.getImages().equals(category.getImages())) {
                String url = storageService.upload(file);
                category.setImages(url);
            }

            categoryService.saveCategory(category);

            redirectAttributes.addFlashAttribute("messageSuccess", "Lưu loại thành công thành công!");
            return "redirect:/admin/category";

    }

    @GetMapping("/admin/category/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            if(productService.countByCategory(id) > 0){
                redirectAttributes.addFlashAttribute("messageError", "Không thể xóa loại sản phẩm vì có sản phẩm loại này đang tồn tại!");
            }else {
                categoryService.deleteCategory(id);
                redirectAttributes.addFlashAttribute("messageSuccess", "Loại sản phẩm với id: " + id + " đã bị xóa thành công");
            }
        }
        catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/admin/category/page/1";
    }


    @PostMapping("/admin/category/check-before-save")
    @ResponseBody
    public String checkBeforeSaveCategory(@RequestParam("name")String name,
                                          @RequestParam("categoryId") Integer categoryId){

        Category categoryByName = categoryService.getCategoryByName(name);
        if(categoryId == null) categoryId = 0;



        if(categoryByName != null && categoryByName.getId() != categoryId){
            return "Duplicate name";
        }

        return "OK";

    }
}
