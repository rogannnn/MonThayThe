package com.example.mon_thay_the.dto;

import com.example.mon_thay_the.entity.PromotionDetail;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PromotionDetailDto {

    private Integer id;
    private Integer percentage;
    private ProductDto productDto;

    public PromotionDetailDto(PromotionDetail promotionDetail){
        this.id =promotionDetail.getId();
        this.percentage = promotionDetail.getPercentage();
        this.productDto = new ProductDto(promotionDetail.getProduct());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }
}
