package com.example.tttndemo.service;

import com.example.tttndemo.repository.ProductRepository;
import com.example.tttndemo.repository.TopSellingProductRepository;
import com.example.tttndemo.utils.TopSellingProduct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
