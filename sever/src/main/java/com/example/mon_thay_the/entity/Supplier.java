package com.example.mon_thay_the.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "supplier")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", unique = true, length = 100)
    private String name;

    @Column(name="email", unique = true, length = 64, nullable = false)
    private String email;

    @Column(name="phone", length = 11, nullable = false)
    private String phone;

    @Column(name="created_at", nullable = false)
    private Date createdAt;


}
