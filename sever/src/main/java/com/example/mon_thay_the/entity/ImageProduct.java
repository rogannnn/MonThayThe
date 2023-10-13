package com.example.mon_thay_the.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "image_product")
public class ImageProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String path;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    public ImageProduct() {

    }

    public ImageProduct(String s){
        this.path = s;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ImageProduct(String path, Product product) {
        this.path = path;
        this.product = product;
    }
}
