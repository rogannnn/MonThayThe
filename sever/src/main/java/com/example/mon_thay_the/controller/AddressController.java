package com.example.mon_thay_the.controller;

import com.example.mon_thay_the.dto.AddressDto;
import com.example.mon_thay_the.dto.DistrictDto;
import com.example.mon_thay_the.dto.ProvinceDto;
import com.example.mon_thay_the.dto.WardDto;
import com.example.mon_thay_the.entity.*;
import com.example.mon_thay_the.exception.AddressNotFoundException;
import com.example.mon_thay_the.exception.NotFoundException;
import com.example.mon_thay_the.exception.UserNotFoundException;
import com.example.mon_thay_the.principal.MyUserDetails;
import com.example.mon_thay_the.request.AddressRequest;
import com.example.mon_thay_the.response.*;
import com.example.mon_thay_the.service.AddressService;
import com.example.mon_thay_the.service.UserService;
import com.google.j2objc.annotations.RetainedWith;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    private final UserService userService;

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

    @GetMapping("/api/district/{id}")
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


    @GetMapping("/api/addresses")
    public ResponseEntity<ListAddressResponse> getListAddressesByUser(HttpServletRequest request
            , @AuthenticationPrincipal MyUserDetails userDetails) throws UserNotFoundException {

        User user = userService.getUserByEmail(userDetails.getUsername());
        List<Address> addressList = addressService.getListAddress(user.getId());

        List<AddressDto> addressDtoList = new ArrayList<>();
        for(Address a : addressList){
            addressDtoList.add(new AddressDto(a));
        }

        ListAddressResponse data = new ListAddressResponse(1,"Get list address successfully!", request.getMethod(), HttpStatus.OK.value(), addressDtoList);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/api/address/{id}")
    public ResponseEntity<AddressResponse> getAddressById(HttpServletRequest request
            , @AuthenticationPrincipal MyUserDetails userDetails
            , @PathVariable("id") Integer addressId) throws UserNotFoundException, AddressNotFoundException {

        User user = userService.getUserByEmail(userDetails.getUsername());
        Address address = addressService.getAddressByUserAndAddressId(user,addressId);

        AddressDto addressDto = new AddressDto(address);
        AddressResponse data = new AddressResponse(1,"Get address successfully!", request.getMethod(), HttpStatus.OK.value(), addressDto);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @GetMapping("/api/address/default")
    public ResponseEntity<AddressResponse> getAddressDefault(HttpServletRequest request
            , @AuthenticationPrincipal MyUserDetails userDetails) throws UserNotFoundException {

        User user = userService.getUserByEmail(userDetails.getUsername());
        Address defaultAddress = addressService.getDefaultAddress(user);

        AddressDto addressDto;

        if(defaultAddress == null){
            addressDto = null;
        }else addressDto = new AddressDto(defaultAddress);


        AddressResponse data = new AddressResponse(1,"Get address default successfully!", request.getMethod(), HttpStatus.OK.value(), addressDto);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/api/address/new")
    public ResponseEntity<AddressResponse> addNewAddress(HttpServletRequest request
            , @AuthenticationPrincipal MyUserDetails myUserDetails
            , @Valid @RequestBody AddressRequest addressRequest) throws UserNotFoundException, AddressNotFoundException, NotFoundException {

        User user = userService.getUserByEmail(myUserDetails.getUsername());

        boolean hasDefault = addressService.isUserHasDefaultAddress(user);
        Ward ward = addressService.getWardByCode(addressRequest.getWardCode());

        if(!hasDefault) addressRequest.setIsDefault(true);

        Address address = new Address();
        address.setName(addressRequest.getName());
        address.setSpecificAddress(addressRequest.getSpecificAddress());
        address.setPhone(addressRequest.getPhone());
        address.setUser(user);
        address.setDefault(addressRequest.getIsDefault());
        address.setWard(ward);


        Address savedAddress = addressService.saveAddress(address);
        addressService.setNonDefault(savedAddress.getId(), user.getId());
        AddressDto addressDto = new AddressDto(savedAddress);

        AddressResponse data = new AddressResponse(1,"Save new address successfully!",request.getMethod(), HttpStatus.CREATED.value(), addressDto);
        return new ResponseEntity<>(data, HttpStatus.CREATED);


    }

    @PutMapping("/api/address/update/{id}")
    private ResponseEntity<AddressResponse> updateAddress(@PathVariable("id") Integer addressId,
            @Valid @RequestBody AddressRequest addressRequest,
            HttpServletRequest request,
            @AuthenticationPrincipal MyUserDetails myUserDetails) throws UserNotFoundException, AddressNotFoundException, NotFoundException {

        User user = userService.getUserByEmail(myUserDetails.getUsername());
        Address address = addressService.getAddressById(addressId);
        Ward ward = addressService.getWardByCode(addressRequest.getWardCode());

        address.setName(addressRequest.getName());
        address.setSpecificAddress(addressRequest.getSpecificAddress());
        address.setPhone(addressRequest.getPhone());
        address.setUser(user);
        address.setDefault(addressRequest.getIsDefault());
        address.setWard(ward);

        Address savedAddress = addressService.saveAddress(address);
        addressService.setNonDefault(savedAddress.getId(), user.getId());
        AddressDto addressDto = new AddressDto(savedAddress);

        AddressResponse data = new AddressResponse(1,"Update address successfully!",request.getMethod(), HttpStatus.OK.value(), addressDto);
        return new ResponseEntity<>(data, HttpStatus.OK);

    }


    @DeleteMapping("/api/address/delete/{id}")
    public ResponseEntity<BaseResponse> deleteAddress(@PathVariable("id") Integer addressId,
            HttpServletRequest request
            , @AuthenticationPrincipal MyUserDetails myUserDetails) throws Exception {

        User user = userService.getUserByEmail(myUserDetails.getUsername());
        Address address = addressService.getAddressByUserAndAddressId(user, addressId);

        if (address.getDefault()){
            throw new Exception("Can not delete default address");
        }else addressService.delete(addressId);

        BaseResponse data = new BaseResponse(1,"Delete address successfully!", request.getMethod(), HttpStatus.OK.value());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/api/address/default/{id}")
    public ResponseEntity<BaseResponse> setDefaultAddress(@PathVariable("id") Integer addressId,
            HttpServletRequest request
            ,@AuthenticationPrincipal MyUserDetails myUserDetails) throws UserNotFoundException, AddressNotFoundException {

        User user = userService.getUserByEmail(myUserDetails.getUsername());
        addressService.setDefaultAddress(addressId,user.getId());

        BaseResponse data = new BaseResponse(1,"Change default address successfully!", request.getMethod(), HttpStatus.OK.value());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }




}
