package com.example.mon_thay_the.dto;


import com.example.mon_thay_the.entity.District;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class DistrictDto {

    private String code;
    private String name;
    private String fullName;
    private ProvinceDto province;

    public DistrictDto(District district){
        this.code = district.getCode();
        this.name = district.getName();
        this.fullName = district.getFullName();
        this.province = new ProvinceDto(district.getProvince());
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

    public ProvinceDto getProvince() {
        return province;
    }

    public void setProvince(ProvinceDto province) {
        this.province = province;
    }
}
