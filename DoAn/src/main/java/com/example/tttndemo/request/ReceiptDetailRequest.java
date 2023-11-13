package com.example.tttndemo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDetailRequest {
    private Integer productId;
    private String productName;
    private Float unitPrice;
    private Integer quantity;
}
