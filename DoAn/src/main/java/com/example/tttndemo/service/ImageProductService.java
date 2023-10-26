package com.example.tttndemo.service;

import com.example.tttndemo.entity.ImageProduct;
import com.example.tttndemo.repository.ImageProductRepository;
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
