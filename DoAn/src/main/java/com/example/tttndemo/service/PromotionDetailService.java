package com.example.tttndemo.service;


import com.example.tttndemo.entity.PromotionDetail;
import com.example.tttndemo.repository.PromotionDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionDetailService {

    private final PromotionDetailRepository promotionDetailRepository;

    public PromotionDetailService(PromotionDetailRepository promotionDetailRepository) {
        this.promotionDetailRepository = promotionDetailRepository;
    }

    public void saveAll(List<PromotionDetail> detailList) {
        promotionDetailRepository.saveAll(detailList);
    }

    public void deleteAll(Integer id) {
        List<PromotionDetail> detailList = promotionDetailRepository.findAllDetailByPromotionId(id);
        promotionDetailRepository.deleteAll(detailList);
    }

    public Integer findDiscountByProductId(Integer productId){
        Integer discount = promotionDetailRepository.FindHighestPercentageForProduct(productId);
        return discount;
    }
}
