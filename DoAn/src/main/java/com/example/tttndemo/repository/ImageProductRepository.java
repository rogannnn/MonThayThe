package com.example.tttndemo.repository;

import com.example.tttndemo.entity.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageProductRepository extends JpaRepository<ImageProduct, Integer> {
    @Query("DELETE FROM ImageProduct i WHERE i.product.id = ?1")
    void deleteAllByProductId(Integer id);
}
