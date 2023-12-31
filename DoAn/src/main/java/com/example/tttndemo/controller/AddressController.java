package com.example.tttndemo.controller;

import com.example.tttndemo.entity.*;
import com.example.tttndemo.exception.AddressNotFoundException;
import com.example.tttndemo.service.AddressService;
import com.example.tttndemo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;

    public AddressController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    @GetMapping("/address")
    public String getAddress(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);

        List<Address> listAddress = addressService.getListAddress(user.getId());
        model.addAttribute("listAddress", listAddress);
        return "address/address";
    }
    @GetMapping("/address/new")
    public String createNewAdress(Model model){

        List<Province> listProvinces = addressService.getListProvince();

        Address newAddress = new Address();

        model.addAttribute("address",newAddress);
        model.addAttribute("listProvinces",listProvinces);
        return "address/address_form";
    }


    @GetMapping("/address/update/{id}")
    public String updateAddress(Model model, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            Address address = addressService.getAddressById(id);
            model.addAttribute("address",address);
            model.addAttribute("wardCode",address.getWard().getCode());
            model.addAttribute("districtCode",address.getWard().getDistrict().getCode());
            model.addAttribute("provinceCode",address.getWard().getDistrict().getProvince().getCode());
            return "address/address_form";
        }catch (AddressNotFoundException ex){
            redirectAttributes.addFlashAttribute("message","Không thể tìm thấy địa chỉ với id = "+ id);
            return "redirect:/address";
        }
    }
    @GetMapping("/address/delete/{id}")
    public String deleteAddress(Model model, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try {
            addressService.delete(id);
            redirectAttributes.addFlashAttribute("message","Xóa địa chỉ thành công!");
            return "redirect:/address";
        }catch (AddressNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex);
            return "redirect:/address";
        }
    }

    @PostMapping("address/save")

    public String saveAddress(Address address, RedirectAttributes redirectAttributes) throws AddressNotFoundException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        address.setUser(user);
        Address savedAddress =  addressService.saveAddress(address);
        if(savedAddress.getDefault() == null && address.getId() == null){
            redirectAttributes.addFlashAttribute("message","Địa chỉ của bạn đã được thêm mới thành công!");
            return "redirect:/address";
        }
        if(savedAddress.getDefault() != false && address.getId() == null){
            addressService.setDefaultAddress(savedAddress.getId(),user.getId());
            redirectAttributes.addFlashAttribute("message","Địa chỉ của bạn đã được thêm mới thành công!");
            return "redirect:/address";
        }
        redirectAttributes.addFlashAttribute("message","Địa chỉ của bạn đã được cập nhật thành công!");
        return "redirect:/address";
    }

//    @PostMapping("address/save")
//    @ResponseBody
//    public String saveAddress(@RequestParam("id") Integer id,
//                              @RequestParam("name") String name,
//                              @RequestParam("phone") String phone,
//                              @RequestParam("specificAddress") String specificAddress,
//                              @RequestParam("isDefault") Boolean isDefault,
//                              @RequestParam("wardCode") String wardCode) throws AddressNotFoundException {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.getCurrentlyLoggedInUser(auth);
//
//        Address address;
//
//        if(id != null){
//            address = new Address();
//        }else address = addressService.getAddressById(id);
//        address.setName(name);
//        address.setSpecificAddress(specificAddress);
//        address.setPhone(phone);
//        address.setUser(user);
//        address.setWard(addressService.getWardByCode(wardCode));
//        address.setUser(user);
//        address.setDefault(isDefault);
//
//        Address savedAddress =  addressService.saveAddress(address);
//        if(id == null){
//            return "add new address successfully!";
//        }else return "updated  address successfully!";
//
//    }

    @GetMapping("address/province")
    @ResponseBody
    public List<Province> getListProvince(){
        List<Province> listProvinces = addressService.getListProvince();
        return listProvinces;
    }

    @GetMapping("/address/district/{provinceCode}")
    @ResponseBody
    public List<District> getDistrictByProvince(@PathVariable("provinceCode") String code){
        return addressService.getListDistrict(code);
    }

    @GetMapping("/address/ward/{districtCode}")
    @ResponseBody
    public List<Ward> getWardByDistrict(@PathVariable("districtCode") String code){
        return addressService.getListWard(code);
    }

    @GetMapping("/address/all")
    @ResponseBody
    public List<Address> getListAddress(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        List<Address> addressList = addressService.getListAddress(user.getId());
        return addressList;
    }

    @GetMapping("/address/{id}")
    @ResponseBody
    public Address getListAddress(@PathVariable("id") Integer addressId) throws AddressNotFoundException {
        return addressService.getAddressById(addressId);
    }

    @GetMapping("address/default/{addressId}")
    public String setDefaultAddress(@PathVariable("addressId") Integer addressId, RedirectAttributes redirectAttributes){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getCurrentlyLoggedInUser(auth);
            addressService.setDefaultAddress(addressId,user.getId());
            redirectAttributes.addFlashAttribute("message","Đổi địa chỉ mặc định thành công!");
        }catch (AddressNotFoundException ex){
            redirectAttributes.addFlashAttribute("message","Có lỗi xảy ra khi đặt địa chỉ mặc định");
            return "redirect:/address";
        }
        return "redirect:/address";
    }

    @PostMapping("/address/new")
    @ResponseBody
    public String addNewAddress(@RequestParam("name") String name,
                                @RequestParam("phone") String phone,
                                @RequestParam("specificAddress") String specificAddress,
                                @RequestParam("wardCode") String wardCode){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        Ward ward = addressService.getWardByCode(wardCode);

        boolean hasDefault = addressService.isUserHasDefault(user);

        Address address = new Address();
        address.setName(name);
        address.setPhone(phone);
        address.setSpecificAddress(specificAddress);

        if(hasDefault){
            address.setDefault(false);
        }else  address.setDefault(true);
        address.setWard(ward);
        address.setUser(user);
        Address savedAddress = addressService.saveAddress(address);

        if (savedAddress != null) return "save new address successfully";

        return "save new address fail";
    }
}