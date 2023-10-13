package com.example.mon_thay_the.service;

import com.example.mon_thay_the.entity.Product;
import com.example.mon_thay_the.exception.ProductNotFoundException;
import com.example.mon_thay_the.repository.ImageProductRepository;
import com.example.mon_thay_the.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    public final static int PRODUCT_PER_PAGE = 6;

    private final ProductRepository productRepository;
    private final ImageProductRepository imageProductRepository;



    public ProductService(ProductRepository productRepository, ImageProductRepository imageProductRepository) {
        this.productRepository = productRepository;

        this.imageProductRepository = imageProductRepository;
    }


    public List<Product> getAll(){
        return productRepository.findAllProductAvaiable();
    }

    public Product saveProduct(Product product) {

        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) throws ProductNotFoundException {
        Long count = productRepository.countById(id);
        if (count == null || count == 0) {
            throw new ProductNotFoundException("Could not find any product with ID " + id);
        }

        productRepository.deleteById(id);
    }
    public Product getProductById(Integer productId) throws ProductNotFoundException{
        Product p = productRepository.findById(productId).get();
        if(p == null) throw new ProductNotFoundException("Could not found product with id: " + productId);
        return p;
    }

    public Product getProductByName(String name) {
        Product product = productRepository.getProductByName(name);

        return product;
    }
    @Transactional
    public List<Product> getTopSellingProduct(int limit){
        return productRepository.getTopSellingProducts(limit);
    }

    public Page<Product> findAllPageByKeyword(int pageNumber , String keyword, String sortField, String sortDir, Integer categoryId, Integer brandId){
        Pageable pageable = null;
        Sort sort = null;
        if(sortField == null){
            pageable = PageRequest.of(pageNumber-1, PRODUCT_PER_PAGE);
        }else {
            sort = Sort.by(sortField);
            sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
            pageable = PageRequest.of(pageNumber-1, PRODUCT_PER_PAGE, sort);
        }
        if(keyword == null) {
            if(categoryId != null && brandId != null){
                return productRepository.findAllByCategoryAndBrand(categoryId, brandId, pageable);
            }else if(categoryId != null && categoryId != 0){
                return productRepository.findAllByCategory(categoryId,pageable);
            }else if(brandId != null && brandId != 0){
                return productRepository.findAllByBrand( brandId, pageable);
            }
        }else {
            return productRepository.findAllByKeyword(keyword,pageable);
        }
        return productRepository.findAllProductAvaiable1(pageable);
    }

    public Page<Product> listByPage(Integer pageNum, String keyword, String sortField, String sortDir) {
        Pageable pageable = null;

        if(sortField != null && !sortField.isEmpty()) {
            Sort sort = Sort.by(sortField);
            sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
            pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE, sort);
        }
        else {
            pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE);
        }

        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findAll(keyword, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public Long countByCategory(Integer categoryId){
        return productRepository.countProductByCategory(categoryId);
    }
    public Long countByBrand(Integer brandId){
        return productRepository.countProductByBrand(brandId);
    }

    public List<String> getListImageProductByProductId(Integer productId) {
        return imageProductRepository.getImageProductByProductId(productId);
    }
}

