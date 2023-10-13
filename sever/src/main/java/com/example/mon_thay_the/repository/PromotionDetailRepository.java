package com.example.mon_thay_the.repository;


import com.example.mon_thay_the.entity.PromotionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, Integer> {
}
