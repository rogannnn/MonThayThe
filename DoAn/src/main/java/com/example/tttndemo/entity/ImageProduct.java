package com.example.tttndemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "image_product")
public class ImageProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String path;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    public ImageProduct() {

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
