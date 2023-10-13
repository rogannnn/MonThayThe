package com.example.mon_thay_the.repository;

import com.example.mon_thay_the.entity.Promotion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    
    Long countById(Integer id);
}
