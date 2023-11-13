package com.example.tttndemo.service;

import com.example.tttndemo.repository.ProductRepository;

import com.example.tttndemo.utils.TopSellingProduct;
import jakarta.transaction.Transactional;
import org.hibernate.query.spi.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class StatisticalService {
    private final ProductRepository productRepository;

    public StatisticalService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Transactional
    public List<TopSellingProduct> getTopSellingProducts(Date startDate, Date endDate, int limitCount) {
        return  productRepository.getTopSellingProducts(startDate, endDate, PageRequest.of(0, limitCount));

    }
}
