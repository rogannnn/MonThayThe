package com.example.mon_thay_the.controller;

import com.example.mon_thay_the.dto.DistrictDto;
import com.example.mon_thay_the.dto.ProvinceDto;
import com.example.mon_thay_the.dto.WardDto;
import com.example.mon_thay_the.entity.District;
import com.example.mon_thay_the.entity.Province;
import com.example.mon_thay_the.entity.Ward;
import com.example.mon_thay_the.response.ListDistrictResponse;
import com.example.mon_thay_the.response.ListProductResponse;
import com.example.mon_thay_the.response.ListProvinceReponse;
import com.example.mon_thay_the.response.ListWardResponse;
import com.example.mon_thay_the.service.AddressService;
import com.google.j2objc.annotations.RetainedWith;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/api/provinces")
    public ResponseEntity<ListProvinceReponse> getListProvince(HttpServletRequest request){

        List<Province> provinceList = addressService.getListProvince();
        List<ProvinceDto> provinceDtoList = new ArrayList<>();

        for(Province p : provinceList){
            provinceDtoList.add(new ProvinceDto(p));
        }

        ListProvinceReponse data = new ListProvinceReponse(1,"Get list province successfully!",request.getMethod(), HttpStatus.OK.value(), provinceDtoList);
        return new  ResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/api/districts/{id}")
    public ResponseEntity<ListDistrictResponse> getListDistrict(@PathVariable("id") String provinceId,
            HttpServletRequest request){

        List<District> districtList = addressService.getListDistrict(provinceId);
        List<DistrictDto> districtDtoList = new ArrayList<>();

        for(District d : districtList){
            districtDtoList.add(new DistrictDto(d));
        }

        ListDistrictResponse data = new ListDistrictResponse(1,"Get list district successfully!",request.getMethod(), HttpStatus.OK.value(), districtDtoList);
        return new  ResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/api/ward/{id}")
    public ResponseEntity<ListWardResponse> getListWard(@PathVariable("id") String districtId,
                                                                HttpServletRequest request){

        List<Ward> wardList = addressService.getListWard(districtId);
        List<WardDto> wardDtoList = new ArrayList<>();

        for(Ward w : wardList){
            wardDtoList.add(new WardDto(w));
        }

        ListWardResponse data = new ListWardResponse(1,"Get list ward successfully!",request.getMethod(), HttpStatus.OK.value(), wardDtoList);
        return new  ResponseEntity(data, HttpStatus.OK);
    }


}
