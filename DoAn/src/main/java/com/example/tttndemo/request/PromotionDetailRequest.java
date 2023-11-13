package com.example.tttndemo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDetailRequest {

    private Integer productId;
    private Integer percentage;
}
