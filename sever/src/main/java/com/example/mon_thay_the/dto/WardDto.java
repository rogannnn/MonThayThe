package com.example.mon_thay_the.dto;

import com.example.mon_thay_the.entity.Ward;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class WardDto {
    private String code;
    private String name;
    private String fullName;
    private DistrictDto district;

    public WardDto(Ward ward){
        this.code = ward.getCode();
        this.name = ward.getName();
        this.fullName = ward.getFullName();
        this.district = new DistrictDto(ward.getDistrict());
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

    public DistrictDto getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDto district) {
        this.district = district;
    }
}
