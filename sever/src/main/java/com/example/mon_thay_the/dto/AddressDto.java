package com.example.mon_thay_the.dto;


import com.example.mon_thay_the.entity.Address;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Integer id;
    private String specificAddress;
    private String name;
    private String phone;
    private boolean isDefault;
    private WardDto ward;


    public AddressDto(Address address){
        this.id = address.getId();
        this.name = address.getName();
        this.phone = address.getPhone();
        this.specificAddress = address.getSpecificAddress();
        this.isDefault = address.getDefault();
        this.ward = new WardDto(address.getWard());

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpecificAddress() {
        return specificAddress;
    }

    public void setSpecificAddress(String specificAddress) {
        this.specificAddress = specificAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public WardDto getWard() {
        return ward;
    }

    public void setWard(WardDto ward) {
        this.ward = ward;
    }
}
