package com.example.mon_thay_the.service;


import com.example.mon_thay_the.repository.TopSellingProductRepository;
import com.example.mon_thay_the.utils.TopSellingProduct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service

public class StatisticalService {
    private final TopSellingProductRepository topSellingProductRepository;

    public StatisticalService(TopSellingProductRepository topSellingProductRepository) {
        this.topSellingProductRepository = topSellingProductRepository;
    }
    @Transactional
    public List<TopSellingProduct> getTopSellingProducts(Date startDate, Date endDate, int limitCount) {
        return topSellingProductRepository.getTopSellingProducts(startDate, endDate, limitCount);
    }
}
