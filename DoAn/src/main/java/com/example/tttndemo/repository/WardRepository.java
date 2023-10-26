package com.example.tttndemo.repository;

import com.example.tttndemo.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {
    @Query("SELECT w FROM Ward w WHERE w.district.code like ?1")
    List<Ward> findByDistrict(String districtCode);
}

