package com.example.mon_thay_the.dto;

import com.example.mon_thay_the.entity.Province;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDto {
    private String code;
    private String name;
    private String fullName;

    public ProvinceDto(Province province){
        this.code = province.getCode();
        this.name = province.getName();
        this.fullName = province.getFullName();
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
