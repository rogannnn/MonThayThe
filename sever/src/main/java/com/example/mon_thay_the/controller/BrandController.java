package com.example.mon_thay_the.controller;

import com.example.mon_thay_the.dto.BrandDto;

import com.example.mon_thay_the.entity.Brand;

import com.example.mon_thay_the.exception.BrandNotFoundException;
import com.example.mon_thay_the.request.BrandRequest;
import com.example.mon_thay_the.response.BaseResponse;
import com.example.mon_thay_the.response.BrandResponse;
import com.example.mon_thay_the.response.ListBrandResponse;
import com.example.mon_thay_the.service.BrandService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BrandController {


    private final BrandService brandService;

    @GetMapping("/api/brands")
    public ResponseEntity<ListBrandResponse> getListBrand(HttpServletRequest request){
        List<Brand> brandList = brandService.listAll();

        List<BrandDto> brandDtoList = new ArrayList<>();

        for(Brand b : brandList){
            brandDtoList.add(new BrandDto(b));
        }

        ListBrandResponse data = new ListBrandResponse(1,"Get list brand successfully!",request.getMethod(), HttpStatus.OK.value(), brandDtoList);

        return new  ResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/api/brand/{id}")
    public ResponseEntity<?> getDetailBrand(@PathVariable("id") Integer brandId,
                                            HttpServletRequest request) throws BrandNotFoundException {

        Brand brand = brandService.getBrandById(brandId);
        BrandDto brandDto = new BrandDto(brand);

        BrandResponse data = new BrandResponse(1,"Get brand successfully!", request.getMethod(), HttpStatus.OK.value(), brandDto);
        return new ResponseEntity(data, HttpStatus.OK);
    }

    @PostMapping("/api/brand/new")
    public ResponseEntity<?> addNewBrand(@Valid @RequestBody BrandRequest brandRequest,
                                            HttpServletRequest request)
    {
        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brand.setDescription(brandRequest.getDescription());
        brand.setEnabled(brandRequest.getEnabled());

        brandService.saveBrand(brand);
        BaseResponse data = new BaseResponse(1,"Add new brand successfully!", request.getMethod(),HttpStatus.CREATED.value());
        return new ResponseEntity(data, HttpStatus.CREATED);
    }

    @PutMapping("/api/brand/update/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable("id") Integer brandId,
                                            @RequestBody BrandRequest brandRequest,
                                            HttpServletRequest request) throws BrandNotFoundException {
        Brand brand = brandService.getBrandById(brandId);
        brand.setName(brandRequest.getName());
        brand.setDescription(brandRequest.getDescription());
        brand.setEnabled(brandRequest.getEnabled());

        brandService.saveBrand(brand);
        BaseResponse data = new BaseResponse(1,"Update brand successfully!", request.getMethod(),HttpStatus.OK.value());
        return new ResponseEntity(data, HttpStatus.OK);
    }

    @DeleteMapping("/api/brand/delete/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable("id") Integer brandId,
                                            HttpServletRequest request) throws BrandNotFoundException {
        brandService.deleteBrand(brandId);

        BaseResponse data = new BaseResponse(1,"Delete brand successfully!", request.getMethod(),HttpStatus.OK.value());
        return new ResponseEntity(data, HttpStatus.OK);
    }
}
