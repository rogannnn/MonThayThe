package com.example.tttndemo.service;


import com.example.tttndemo.entity.ReceiptDetail;
import com.example.tttndemo.repository.ReceiptDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptDetailService {

    private final ReceiptDetailRepository receiptDetailRepository;

    public ReceiptDetailService(ReceiptDetailRepository receiptDetailRepository) {
        this.receiptDetailRepository = receiptDetailRepository;
    }

    public void saveAll(List<ReceiptDetail> receiptDetailList) {
        receiptDetailRepository.saveAll(receiptDetailList);
    }
}
