package com.example.mon_thay_the.controller;

import com.example.mon_thay_the.dto.CategoryDto;
import com.example.mon_thay_the.entity.Category;
import com.example.mon_thay_the.exception.CategoryNotFoundException;
import com.example.mon_thay_the.request.CategoryRequest;
import com.example.mon_thay_the.response.BaseResponse;
import com.example.mon_thay_the.response.CategoryResponse;
import com.example.mon_thay_the.response.ListCategoryResponse;
import com.example.mon_thay_the.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    public ResponseEntity<ListCategoryResponse> getListCategory(HttpServletRequest request){
            List<Category> categoryList = categoryService.listAll();

            List<CategoryDto> categoryDtoList = new ArrayList<>();

            for(Category  c : categoryList){
                categoryDtoList.add(new CategoryDto(c));
            }

            ListCategoryResponse data = new ListCategoryResponse(1,"Get list category successfully!",request.getMethod(), HttpStatus.OK.value(), categoryDtoList);

            return new  ResponseEntity(data, HttpStatus.OK);
    }


    @GetMapping("/api/category/{id}")
    public ResponseEntity<?> getDetailCategory(@PathVariable("id") Integer categoryId,
                                            HttpServletRequest request) throws CategoryNotFoundException {

        Category category = categoryService.getCategoryById(categoryId);
        CategoryDto categoryDto = new CategoryDto(category);

        CategoryResponse data = new CategoryResponse(1,"Get category successfully!", request.getMethod(), HttpStatus.OK.value(), categoryDto);
        return new ResponseEntity(data, HttpStatus.OK);
    }

    @PostMapping("/api/category/new")
    public ResponseEntity<?> addNewCategory(@RequestBody CategoryRequest categoryRequest,
                                            HttpServletRequest request)
    {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setImages(categoryRequest.getImage());
        category.setEnabled(categoryRequest.getEnabled());

        categoryService.saveCategory(category);
        BaseResponse data = new BaseResponse(1,"Add new category successfully!", request.getMethod(),HttpStatus.CREATED.value());
        return new ResponseEntity(data, HttpStatus.CREATED);
    }

    @PutMapping("/api/category/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Integer categoryId,
                                            @RequestBody CategoryRequest categoryRequest,
                                            HttpServletRequest request) throws CategoryNotFoundException {
        Category category = categoryService.getCategoryById(categoryId);
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setImages(categoryRequest.getImage());
        category.setEnabled(categoryRequest.getEnabled());

        categoryService.save(category);
        BaseResponse data = new BaseResponse(1,"Update category successfully!", request.getMethod(),HttpStatus.OK.value());
        return new ResponseEntity(data, HttpStatus.OK);
    }

    @DeleteMapping("/api/category/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Integer categoryId,
                                            HttpServletRequest request) throws CategoryNotFoundException {
        categoryService.deleteCategory(categoryId);

        BaseResponse data = new BaseResponse(1,"Delete category successfully!", request.getMethod(),HttpStatus.OK.value());
        return new ResponseEntity(data, HttpStatus.OK);
    }
}
