package com.example.tttndemo.service;

import com.example.tttndemo.entity.Promotion;
import com.example.tttndemo.exception.PromotionNotFoundException;
import com.example.tttndemo.repository.PromotionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PromotionService {

    public static final int PROMOTION_PER_PAGE = 10;
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public Promotion getPromotionById(Integer id){
        return promotionRepository.findById(id).get();
    }

    public void deletePromotion(Integer id) throws PromotionNotFoundException {
        Long countById = promotionRepository.countById(id);
        if(countById == null || countById == 0 ){
            throw new PromotionNotFoundException("Could not found promotion with id :" + id);
        }
        promotionRepository.deleteById(id);
    }


    public Page<Promotion> listByPage(Integer pageNumber, String sortDir, String sortField){

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber-1, PROMOTION_PER_PAGE,sort);

        return promotionRepository.findAll(pageable);
    }

    public Promotion savePromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }
}
