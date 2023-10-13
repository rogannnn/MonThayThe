package com.example.mon_thay_the.repository;


import com.example.mon_thay_the.utils.TopSellingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TopSellingProductRepository extends JpaRepository<TopSellingProduct, Integer>
{
    @Procedure("GetTopSellingProducts")
    List<TopSellingProduct> getTopSellingProducts(Date startDate, Date endDate, int limitCount);
}
