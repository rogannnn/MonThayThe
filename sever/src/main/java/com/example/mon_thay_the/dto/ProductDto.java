package com.example.mon_thay_the.dto;

import com.example.mon_thay_the.entity.ImageProduct;
import com.example.mon_thay_the.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
public class ProductDto {

    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer inStock;
    private Integer soldQuantity;
    private Date createdAt;
    private Boolean enabled;
    private List<ImageProduct> imageProducts = new ArrayList<>();
    private CategoryDto category;
    private BrandDto brand;

    public ProductDto(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.inStock = product.getInStock();
        this.soldQuantity = product.getSoldQuantity();
        this.createdAt = product.getCreatedAt();
        this.enabled = product.getEnabled();

        for(ImageProduct img : product.getImageProducts()){
            this.imageProducts.add(img);
        }
        this.brand = new BrandDto(product.getBrand());
        this.category= new CategoryDto(product.getCategory());
    }

}
