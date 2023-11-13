package com.example.tttndemo.utils;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RevenueItem {

    private String date;
    private Integer totalSold;
    private Long totalRevenue;
    private Long totalFund;
    private Long totalProfit;

}
