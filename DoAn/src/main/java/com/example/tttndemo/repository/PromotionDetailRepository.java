package com.example.tttndemo.repository;

import com.example.tttndemo.entity.PromotionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, Integer> {

    @Query(nativeQuery = true, value = "CALL FindHighestPercentageForProduct(:productId)")
    Integer FindHighestPercentageForProduct(@Param("productId") Integer productId);

    @Query("SELECT p FROM PromotionDetail  p WHERE p.promotion.id = ?1")
    List<PromotionDetail> findAllDetailByPromotionId(Integer id);
}
