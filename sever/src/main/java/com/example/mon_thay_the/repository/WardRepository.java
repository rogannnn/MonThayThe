package com.example.mon_thay_the.repository;


import com.example.mon_thay_the.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {
    @Query("SELECT w FROM Ward w WHERE w.district.code like ?1")
    List<Ward> findByDistrict(String districtCode);


    @Query("SELECT w FROM Ward w WHERE w.code = ?1")
    Ward getWardByCode(String wardCode);
}

