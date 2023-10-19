package com.example.mon_thay_the.service;


import com.example.mon_thay_the.entity.*;
import com.example.mon_thay_the.exception.AddressNotFoundException;
import com.example.mon_thay_the.exception.NotFoundException;
import com.example.mon_thay_the.repository.AddressRepository;
import com.example.mon_thay_the.repository.DistrictRepository;
import com.example.mon_thay_the.repository.ProvinceRepository;
import com.example.mon_thay_the.repository.WardRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AddressService {

    private final AddressRepository addressRepo;
    private final WardRepository wardRepo;
    private final DistrictRepository districtRepo;
    private final ProvinceRepository provinceRepo;

    public AddressService(AddressRepository addressRepo, WardRepository wardRepo, DistrictRepository districtRepo, ProvinceRepository provinceRepo) {
        this.addressRepo = addressRepo;
        this.wardRepo = wardRepo;
        this.districtRepo = districtRepo;
        this.provinceRepo = provinceRepo;
    }


    public List<Province> getListProvince(){
        return provinceRepo.findAll();
    }

    public List<District> getListDistrict(String provinceCode){
        return districtRepo.findByProvince(provinceCode);
    }

    public List<Ward> getListWard(String districtCode){
        return wardRepo.findByDistrict(districtCode);
    }

    public Address saveAddress(Address address){
        return addressRepo.save(address);
    }

    public List<Address> getListAddress(Integer userId){
        return addressRepo.getListAddressByUser(userId);
    }

    public Address getAddressById(Integer addressId) throws AddressNotFoundException {
        try {
            return addressRepo.findById(addressId).get();
        }catch (NoSuchElementException ex){
            throw new AddressNotFoundException("Could not find address with id " + addressId);
        }
    }


    public void delete(Integer addressId) throws AddressNotFoundException {
        Long count = addressRepo.countById(addressId);
        if(count == null || count == 0){
            throw new AddressNotFoundException("Could not found address with id" + addressId);
        }
        addressRepo.deleteById(addressId);
    }

    public void setDefaultAddress(Integer addressId, Integer userId) throws AddressNotFoundException {
        if(addressRepo.existsAddressById(addressId)){
            addressRepo.setDefaultAddress(addressId);
            addressRepo.setNonDefaultAddress(userId,addressId);
        }
        else throw new AddressNotFoundException("Could not found address with id " + addressId);
    }

    public void setNonDefault(Integer addressId, Integer userId) throws AddressNotFoundException {
        if(addressRepo.existsAddressById(addressId)){
            addressRepo.setNonDefaultAddress(userId,addressId);
        }
        else throw new AddressNotFoundException("Could not found address with id " + addressId);
    }

    public Address getDefaultAddress(User user) {
        return addressRepo.findDefaultAddress(user.getId());
    }

    public Boolean isUserHasDefaultAddress(User user){
        Address address = addressRepo.existsAddressByDefaultAndUser(user.getId());
        return address == null;
    }

    public Ward getWardByCode(String wardCode) throws NotFoundException {
        Ward ward = wardRepo.getWardByCode(wardCode);
        if(ward == null) throw new NotFoundException("Could not found ward with ward code: " + wardCode);
        return ward;
    }

    public Address getAddressByUserAndAddressId(User user, Integer addressId) throws AddressNotFoundException {
        Address address = addressRepo.findByUserAndAddressId(user.getId(), addressId);
        if(address == null) throw new AddressNotFoundException("Could not found address id: "+ addressId + " with user ID: " + user.getId());
        return address;
    }
}
