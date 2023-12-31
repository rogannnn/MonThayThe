package com.example.tttndemo.repository;

import com.example.tttndemo.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {
    @Query("SELECT d FROM District d WHERE d.province.code like ?1")
    List<District> findByProvince(String provinceCode);
}

