package com.example.tttndemo.service;


import com.example.tttndemo.entity.Supplier;
import com.example.tttndemo.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    public static final int SUPPLIER_PER_PAGE = 10;

    private final SupplierRepository supplierRepository;


    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Page<Supplier> listByPage(Integer pageNumber, String sortDir, String sortField){

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber-1, SUPPLIER_PER_PAGE,sort);

        return supplierRepository.findAll(pageable);
    }

    public Supplier getSupplierById(Integer supplierId){
        return supplierRepository.findById(supplierId).get();
    }

    public Supplier saveSupplier(Supplier supplier){
        return supplierRepository.save(supplier);
    }

    public Supplier getSupplierByEmail(String email) {
        return supplierRepository.findByEmail(email);
    }

    public Supplier getSupplierByName(String name) {
        return supplierRepository.findByName(name);
    }

    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }
}
