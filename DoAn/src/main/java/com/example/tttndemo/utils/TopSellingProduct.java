package com.example.tttndemo.utils;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


public class TopSellingProduct {


    private Integer id;

    private String name;

    private Long totalSoldQuantity;

    public TopSellingProduct(Integer id, String name, Long totalSoldQuantity) {
        this.id = id;
        this.name = name;
        this.totalSoldQuantity = totalSoldQuantity;
    }

    public TopSellingProduct() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTotalSoldQuantity() {
        return totalSoldQuantity;
    }

    public void setTotalSoldQuantity(Long totalSoldQuantity) {
        this.totalSoldQuantity = totalSoldQuantity;
    }
}
