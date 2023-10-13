package com.example.mon_thay_the.dto;

import com.example.mon_thay_the.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BrandDto {

    private Integer id;
    private String name;
    private String description;

    public BrandDto(Brand brand){
        this.id = brand.getId();
        this.name = brand.getName();
        this.description = brand.getDescription();
    }

}
