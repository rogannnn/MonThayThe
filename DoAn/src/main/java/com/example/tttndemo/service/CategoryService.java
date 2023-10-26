package com.example.tttndemo.service;

import com.example.tttndemo.entity.Category;
import com.example.tttndemo.exception.CategoryNotFoundException;
import com.example.tttndemo.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {
    public static final int CATEGORY_PER_PAGE = 10;
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category get(Integer categoryId) throws CategoryNotFoundException {
        try
        {
            return categoryRepository.findById(categoryId).get();
        }catch (NoSuchElementException ex){
            throw new CategoryNotFoundException("Could not found user with id :" + categoryId);
        }
    }

    public List<Category> listAll(){
        List<Category>  listCategory = categoryRepository.findAll();
        return listCategory;
    }

    public Page<Category> listByPage(Integer pageNumber, String keyword, String sortDir, String sortField){

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber-1, CATEGORY_PER_PAGE,sort);

        if(keyword != null){
            return categoryRepository.findAll(keyword,pageable);
        }
        return categoryRepository.findAll(pageable);
    }

    public Category getCategoryById(Integer id){
        return categoryRepository.findById(id).get();
    }

    public void deleteCategory(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);
        if(countById == null || countById == 0 ){
            throw new CategoryNotFoundException("Could not found category with id :" + id);
        }
        categoryRepository.deleteById(id);
    }


    public void updateCategoryEnabledStatus(Integer categoryId, boolean status){
        categoryRepository.updateEnabledStatus(categoryId,status);
    }

    public Category save(Category categoryInForm){
        if(categoryInForm.getId() != null){
            Category savedCate = categoryRepository.findById(categoryInForm.getId()).get();
            savedCate.setName(categoryInForm.getName());
            savedCate.setDescription(categoryInForm.getDescription());
            savedCate.setEnabled(categoryInForm.getEnabled());
            savedCate.setImages(categoryInForm.getImages());
            return  categoryRepository.save(savedCate);
        }
        else return categoryRepository.save(categoryInForm);
    }

    public String checkUniqueCategoryName(Integer id, String name){
        boolean isCreatingNew = (id == null || id == 0);

        Category categoryByName =  categoryRepository.findByName(name);
        if(isCreatingNew){
            if(categoryByName != null){
                return "DuplicateName";
            }
        }
        return "OK";
    }

    public Category getCategoryByName(String name) {
        Category category = categoryRepository.getCategoriesByName(name);
        return category;
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }
}
