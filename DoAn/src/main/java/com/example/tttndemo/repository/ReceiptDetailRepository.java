package com.example.tttndemo.repository;

import com.example.tttndemo.entity.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Integer> {

    @Query("SELECT AVG(rd.unitPrice) FROM ReceiptDetail rd WHERE rd.product.id = :productId")
    Float findAverageUnitPriceByProductId(@Param("productId") Integer productId);
}
