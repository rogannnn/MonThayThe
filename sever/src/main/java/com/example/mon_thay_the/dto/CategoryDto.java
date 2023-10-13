package com.example.mon_thay_the.dto;


import com.example.mon_thay_the.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto {
    private Integer id;
    private String name;
    private String description;

    public CategoryDto(Category category){
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
    }

}
