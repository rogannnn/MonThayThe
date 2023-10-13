package com.example.mon_thay_the.entity;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "wards")
public class Ward {

    @Id
    @Column(name="code", length = 20, nullable = false)
    private String code;

    @Column(name="name", nullable = false, length = 255)
    private String name;

    @Column(name="full_name", length = 255)
    private String fullName;


    @OneToMany(mappedBy = "ward")
    private Collection<Address> addresses;

    @ManyToOne
    @JoinColumn(name="district_code")
    private District district;


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


    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "Ward{" +
                "code='" + code + '\'' +
                '}';
    }
}
