package com.example.mon_thay_the.dto;


import com.example.mon_thay_the.entity.Promotion;
import com.example.mon_thay_the.entity.PromotionDetail;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class PromotionDto {

    private Integer id;
    private String name;
    private Date startDate;
    private Date finishDate;
    private UserDto userDto;

    private List<PromotionDetailDto> promotionDetailDtoList = new ArrayList<>();


    public PromotionDto(Promotion promotion){
        this.id = promotion.getId();
        this.name = promotion.getName();
        this.startDate = promotion.getStartDate();
        this.finishDate = promotion.getFinishDate();
        this.userDto = new UserDto(promotion.getUser());

        for(PromotionDetail p : promotion.getPromotionDetails()){
            this.promotionDetailDtoList.add(new PromotionDetailDto(p));
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public List<PromotionDetailDto> getList() {
        return promotionDetailDtoList;
    }

    public void setList(List<PromotionDetailDto> list) {
        this.promotionDetailDtoList = list;
    }
}
