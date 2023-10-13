package com.example.mon_thay_the.service;

import com.example.mon_thay_the.entity.ImageProduct;
import com.example.mon_thay_the.repository.ImageProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageProductService {

    private final ImageProductRepository imageProductRepository;

    public ImageProductService(ImageProductRepository imageProductRepository) {
        this.imageProductRepository = imageProductRepository;
    }


    public List<ImageProduct> saveAll(List<ImageProduct> imageProductList) {
        return imageProductRepository.saveAll(imageProductList);
    }

    public void deleteByProductId(Integer id) {
        imageProductRepository.deleteAllByProductId(id);
    }
}
