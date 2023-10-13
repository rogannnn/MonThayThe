package com.example.mon_thay_the.repository;

import com.example.mon_thay_the.entity.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImageProductRepository extends JpaRepository<ImageProduct, Integer> {
    @Query("DELETE FROM ImageProduct i WHERE i.product.id = ?1")
    void deleteAllByProductId(Integer id);


    @Query("SELECT i FROM  ImageProduct i WHERE i.product.id = ?1")
    List<String> getImageProductByProductId(Integer productId);




}
