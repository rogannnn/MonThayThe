package com.example.tttndemo.controller;


import com.example.tttndemo.entity.Supplier;
import com.example.tttndemo.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/")
public class SupplierController {


    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/supplier/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") Integer pageNum, Model model,
                             @RequestParam(defaultValue = "id") String sortField,
                             @RequestParam(required = false) String sortDir) {

        model.addAttribute("sortField", sortField);

        if(sortDir == null) sortDir = "asc";
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);


        Page<Supplier> page = supplierService.listByPage(pageNum, sortDir, sortField);
        List<Supplier> listSuppliers = page.getContent();
        long startCount = (pageNum - 1) * supplierService.SUPPLIER_PER_PAGE + 1;
        long endCount = startCount +  supplierService.SUPPLIER_PER_PAGE - 1;

        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }



        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);

        model.addAttribute("listSuppliers", listSuppliers);

        return "supplier/suppliers";
    }

    @GetMapping("/supplier/add")
    public String addNewSupplier(Model model){
        Supplier supplier = new Supplier();
        model.addAttribute("supplier",supplier);

        return "supplier/supplier_form";

    }

    @GetMapping("/supplier/edit/{id}")
    public String updateSupplier(Model model, @PathVariable("id") Integer id){
        Supplier supplier = supplierService.getSupplierById(id);

       if(supplier != null){
           model.addAttribute("supplier", supplier);
           return "supplier/supplier_form";
       }else return "error/404";
    }

    @PostMapping("/supplier/save")
    public String saveSupplier(RedirectAttributes redirectAttributes,
                               Supplier supplier){

        Supplier existSupplier = null;
        if(supplier.getId() != null){
            existSupplier = supplierService.getSupplierById(supplier.getId());
            supplier.setCreatedAt(existSupplier.getCreatedAt());
        }else {
            supplier.setCreatedAt(new Date());
        }
        supplierService.saveSupplier(supplier);
        redirectAttributes.addFlashAttribute("messageSuccess","Lưu nhà cung cấp thành công");
        return "redirect:/admin/supplier/page/1";
    }

    @PostMapping("/supplier/check-before-save")
    @ResponseBody
    public String checkBeforeSaveSupplier(@RequestParam("name")String name,
                                          @RequestParam("email") String email,
                                          @RequestParam("supplierId") Integer supplierId){

        Supplier supplierByName = supplierService.getSupplierByName(name);
        Supplier supplierByEmail = supplierService.getSupplierByEmail(email);


        if(supplierByEmail == null && supplierByName == null){
            return "OK";
        }

        if(supplierByEmail != null && supplierByEmail.getId() != supplierId){
            return "Duplicate email";
        }

        if(supplierByName != null && supplierByName.getId() != supplierId){
            return "Duplicate name";
        }

        return "OK";

    }



}
