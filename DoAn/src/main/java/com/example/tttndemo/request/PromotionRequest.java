package com.example.tttndemo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromotionRequest {
    private String name;
    private String startDate;
    private String endDate;
    List<PromotionDetailRequest> data ;
}
