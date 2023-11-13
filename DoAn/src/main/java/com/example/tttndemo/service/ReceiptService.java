package com.example.tttndemo.service;


import com.example.tttndemo.entity.Receipt;
import com.example.tttndemo.repository.ReceiptRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {

    public static final int RECEIPT_PER_PAGE = 20;
    private final ReceiptRepository receiptRepository;

    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }



    public Page<Receipt> listByPage(Integer pageNumber, String sortDir, String sortField){

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber-1, RECEIPT_PER_PAGE,sort);

        return receiptRepository.findAll(pageable);
    }

    public Receipt save(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    public Receipt getReceiptById(Integer receiptID) {
        return receiptRepository.findById(receiptID).get();
    }
}
