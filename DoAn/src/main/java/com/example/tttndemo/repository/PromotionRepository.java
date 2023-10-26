package com.example.tttndemo.repository;

import com.example.tttndemo.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    
    Long countById(Integer id);
}
