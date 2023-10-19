package com.example.mon_thay_the.controller;


import com.example.mon_thay_the.dto.ProductDto;
import com.example.mon_thay_the.entity.Brand;
import com.example.mon_thay_the.entity.Category;
import com.example.mon_thay_the.entity.ImageProduct;
import com.example.mon_thay_the.entity.Product;
import com.example.mon_thay_the.exception.BrandNotFoundException;
import com.example.mon_thay_the.exception.CategoryNotFoundException;
import com.example.mon_thay_the.exception.ProductNotFoundException;
import com.example.mon_thay_the.request.ProductRequest;
import com.example.mon_thay_the.response.BaseResponse;
import com.example.mon_thay_the.response.ListProductResponse;
import com.example.mon_thay_the.response.ProductResponse;
import com.example.mon_thay_the.service.BrandService;
import com.example.mon_thay_the.service.CategoryService;
import com.example.mon_thay_the.service.ImageProductService;
import com.example.mon_thay_the.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    private final CategoryService categoryService;

    private final BrandService brandService;

    private final ImageProductService imageProductService;

    @GetMapping("/api/products")
    public ResponseEntity<ListProductResponse> getListProduct(
            @RequestParam(value = "pageNo", required = false) Optional<Integer> pPageNo,
            @RequestParam(value = "pageSize", required = false) Optional<Integer> pPageSize,
            @RequestParam(value = "sortField", required = false) Optional<String> pSortField,
            @RequestParam(value = "sortDirection", required = false) Optional<String> pSortDir,
            @RequestParam(value = "categoryId", required = false) Optional<Integer> pCategoryId,
            @RequestParam(value = "categoryId", required = false) Optional<Integer> pBrandId,
            @RequestParam(value = "keyword", required = false) Optional<String> pKeyword,
            HttpServletRequest request
    ){
        int pageNo = 1;
        int pageSize = 10;
        String sortField = "id";
        String sortDirection = "ASC";
        String keyword = "";
        int brandId = 0, categoryId = 0;

        if (pPageNo.isPresent()) {
            pageNo = pPageNo.get();
        }
        if (pPageSize.isPresent()) {
            pageSize = pPageSize.get();
        }
        if (pSortField.isPresent()) {
            sortField = pSortField.get();
        }
        if (pSortDir.isPresent()) {
            sortDirection = pSortDir.get();
        }
        if(pKeyword.isPresent()){
            keyword = pKeyword.get();
        }
        if(pCategoryId.isPresent()){
            categoryId = pCategoryId.get();
        }
        if(pBrandId.isPresent()){
            brandId = pBrandId.get();
        }

        int totalPage;
        List<Product> products;
        List<ProductDto> productDtos = new ArrayList<ProductDto>();

        Page<Product> pageProduct ;
        if(pageNo == 1){
            pageProduct = productService.findAllPageByKeyword(1, keyword, sortField, sortDirection, categoryId , brandId);

        }else {
            pageProduct = productService.findAllPageByKeyword(pageNo,keyword,sortField,sortDirection, categoryId, brandId);
        }
        products = pageProduct.getContent();

        for(Product p : products){
            productDtos.add(new ProductDto(p));
        }

        ListProductResponse data = new ListProductResponse(1,"Get list products successfully!",request.getMethod(), HttpStatus.OK.value(),productDtos,pageProduct.getTotalPages(),pageNo);
        return new ResponseEntity(data, HttpStatus.OK);
    }


    @GetMapping("/api/product/{id}")
    public ResponseEntity<ProductResponse> getDetailProduct(@PathVariable("id") Integer productId, HttpServletRequest request) throws ProductNotFoundException {
        Product product = productService.getProductById(productId);

//        List<String> listProductImage =productService.getListImageProductByProductId(productId);

        ProductDto productDto =new ProductDto(product);
        ProductResponse data =new ProductResponse(1,"Get product successfully!",request.getMethod(),HttpStatus.OK.value(), productDto);
        return new ResponseEntity(data, HttpStatus.OK);
    }


    @PostMapping("/api/product/new")
    public ResponseEntity<ProductResponse> addNewProduct(@Valid @RequestBody ProductRequest productRequest,HttpServletRequest request) throws BrandNotFoundException, CategoryNotFoundException {
        Category category = categoryService.getCategoryById(productRequest.getCategoryId());
        Brand brand = brandService.getBrandById(productRequest.getBrandId());

        Product product = new Product();

        product.setName(productRequest.getName());
        product.setEnabled(productRequest.getEnabled());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setInStock(productRequest.getInStock());
        product.setSoldQuantity(productRequest.getSoldQuantity());
        product.setBrand(brand);
        product.setCategory(category);
        product.setCreatedAt(new Date());

        List<ImageProduct> imageProductList = new ArrayList<>();

        Product savedProduct =  productService.saveProduct(product);
        for(String img : productRequest.getImages()){
            imageProductList.add(new ImageProduct(img));
        }
        for(ImageProduct img : imageProductList){
            img.setProduct(savedProduct);
        }
        imageProductService.saveAll(imageProductList);
        savedProduct.setImageProducts(imageProductList);
        ProductDto productDto = new ProductDto(savedProduct);

        ProductResponse data = new ProductResponse(1,"Add new product successfully!",request.getMethod(), HttpStatus.CREATED.value(), productDto);
        return  new ResponseEntity(data, HttpStatus.CREATED);

    }


    @PutMapping("/api/product/update/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("id") Integer productId,@Valid @RequestBody ProductRequest productRequest
                                                         ,HttpServletRequest request) throws CategoryNotFoundException, BrandNotFoundException, ProductNotFoundException {
        Category category = categoryService.getCategoryById(productRequest.getCategoryId());
        Brand brand = brandService.getBrandById(productRequest.getBrandId());

        Product product = productService.getProductById(productId);


        product.setName(productRequest.getName());
        product.setEnabled(productRequest.getEnabled());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setInStock(productRequest.getInStock());
        product.setSoldQuantity(productRequest.getSoldQuantity());
        product.setBrand(brand);
        product.setCategory(category);


        List<ImageProduct> imageProductList = new ArrayList<>();
        for(String img : productRequest.getImages()){
            imageProductList.add(new ImageProduct(img));
        }

        //need more code to update image product path

        Product savedProduct =  productService.saveProduct(product);
        savedProduct.setImageProducts(imageProductList);
        ProductDto productDto = new ProductDto(savedProduct);

        ProductResponse data = new ProductResponse(1,"Update product successfully!",request.getMethod(), HttpStatus.CREATED.value(), productDto);
        return  new ResponseEntity(data, HttpStatus.OK);

    }


    @DeleteMapping("/api/product/delete/{id}")
    public ResponseEntity<BaseResponse> deleteProduct(@PathVariable("id") Integer productId, HttpServletRequest request) throws ProductNotFoundException {
        productService.deleteProduct(productId);

        BaseResponse data = new BaseResponse(1,"Delete product successfully!", request.getMethod(), HttpStatus.OK.value());
        return new ResponseEntity(data, HttpStatus.OK);
    }

}
