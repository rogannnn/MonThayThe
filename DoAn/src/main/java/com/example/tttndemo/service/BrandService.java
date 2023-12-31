package com.example.tttndemo.service;

import com.example.tttndemo.entity.Brand;
import com.example.tttndemo.exception.BrandNotFoundException;
import com.example.tttndemo.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {

    public static final int BRAND_PER_PAGE = 9;

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> listAll(){
        return brandRepository.findAll();
    }

    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public Page<Brand> listByPage(Integer pageNum, String keyword, String sortField, String sortDir) {
        Pageable pageable = null;

        if(sortField != null && !sortField.isEmpty()) {
            Sort sort = Sort.by(sortField);
            sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
            pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE, sort);
        }
        else {
            pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE);
        }

        if (keyword != null && !keyword.isEmpty()) {
            return brandRepository.findAll(keyword, pageable);
        }
        return brandRepository.findAll(pageable);
    }

    public Brand findBrandByName(String name) {
        Brand brand = brandRepository.findBrandByName(name);
        return brand;
    }

    public Brand getBrandById(Integer id) throws BrandNotFoundException {
        try {
            Brand brand = brandRepository.findById(id).get();
            return brand;
        }
        catch(NoSuchElementException ex) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);

        }
    }

    public void deleteBrand(Integer id) throws BrandNotFoundException {
        Long count = brandRepository.countById(id);
        if (count == null || count == 0) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }

        brandRepository.deleteById(id);
    }

    public Brand getBrandByName(String name) {
        return brandRepository.findBrandByName(name);
    }
}
