package com.example.tttndemo.dto;

import com.example.tttndemo.entity.Brand;
import com.example.tttndemo.entity.Category;
import com.example.tttndemo.entity.ImageProduct;
import com.example.tttndemo.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Double priceDiscount;
    private Integer discount;
    private Integer inStock;
    private Integer soldQuantity;
    private Category category;
    private Brand brand;
    private List<ImageProduct> imageProducts;


    public ProductDTO(Integer discount, Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.discount = discount;
        if(discount == null) {
            this.priceDiscount = price;
        }else {
            this.priceDiscount = price - (price * discount / 100);
        }
        this.inStock = product.getInStock();
        this.soldQuantity = product.getSoldQuantity();
        this.brand = product.getBrand();
        this.category = product.getCategory();
        this.imageProducts = product.getImageProducts();
    }
}
