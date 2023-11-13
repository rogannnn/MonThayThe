package com.example.tttndemo.service;

import com.example.tttndemo.entity.*;
import com.example.tttndemo.exception.AddressNotFoundException;
import com.example.tttndemo.repository.AddressRepository;
import com.example.tttndemo.repository.DistrictRepository;
import com.example.tttndemo.repository.ProvinceRepository;
import com.example.tttndemo.repository.WardRepository;
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

    public Address getDefaultAddress(User user) {
        return addressRepo.findDefaultAddress(user.getId());
    }

    public Ward getWardByCode(String wardCode){
        return wardRepo.findById(wardCode).get();
    }

    public boolean isUserHasDefault(User user){
        Address address = addressRepo.getAddressByDefault(user.getId());
        return address != null;
    }
}
