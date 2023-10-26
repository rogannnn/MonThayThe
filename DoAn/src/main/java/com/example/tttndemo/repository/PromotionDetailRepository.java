package com.example.tttndemo.repository;

import com.example.tttndemo.entity.PromotionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, Integer> {
}
