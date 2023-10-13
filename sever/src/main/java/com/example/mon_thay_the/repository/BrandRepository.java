package com.example.mon_thay_the.repository;


import com.example.mon_thay_the.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    Long countById(Integer id);

    Brand findByName(String name);

    @Query("UPDATE Brand b set b.enabled = ?2 WHERE b.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer BrandId, boolean enabled);

    @Query("SELECT b FROM Brand b WHERE CONCAT(b.id, ' ',b.name) LIKE %?1%")
    Page<Brand> findAll(String keyword, Pageable pageable);

    Brand findBrandByName(String name);
}
