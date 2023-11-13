package com.example.tttndemo.controller;

import com.example.tttndemo.entity.Product;
import com.example.tttndemo.entity.Receipt;
import com.example.tttndemo.entity.ReceiptDetail;
import com.example.tttndemo.entity.Supplier;
import com.example.tttndemo.request.ReceiptDetailRequest;
import com.example.tttndemo.request.ReceiptRequest;
import com.example.tttndemo.service.ProductService;
import com.example.tttndemo.service.ReceiptDetailService;
import com.example.tttndemo.service.ReceiptService;
import com.example.tttndemo.service.SupplierService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ReceiptController {

    private final ReceiptService receiptService;
    private final SupplierService supplierService;
    private final ReceiptDetailService receiptDetailService;
    private final ProductService productService;

    public ReceiptController(ReceiptService receiptService, SupplierService supplierService, ReceiptDetailService receiptDetailService, ProductService productService) {
        this.receiptService = receiptService;
        this.supplierService = supplierService;
        this.receiptDetailService = receiptDetailService;
        this.productService = productService;
    }


    @GetMapping("/receipt/page/{pageNum}")
    public String listByReceiptPage(@PathVariable(name = "pageNum") Integer pageNum, Model model,
                                    @RequestParam(defaultValue = "id") String sortField,
                                    @RequestParam(required = false) String sortDir) {

        model.addAttribute("sortField", sortField);

        if(sortDir == null) sortDir = "asc";
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);


        Page<Receipt> page = receiptService.listByPage(pageNum, sortDir, sortField);
        List<Receipt> listReceipts = page.getContent();
        long startCount = (pageNum - 1) * receiptService.RECEIPT_PER_PAGE + 1;
        long endCount = startCount +  receiptService.RECEIPT_PER_PAGE - 1;

        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }



        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);

        model.addAttribute("listReceipts", listReceipts);


        return "receipt/receipts";
    }

    @GetMapping("/receipt/add")
    public String addReceipt(Model model) {


        List<Supplier> listSuppliers = supplierService.getAll();
        List<Product> listProducts = productService.getAll();
        model.addAttribute("listSuppliers", listSuppliers);
        model.addAttribute("listProducts", listProducts);

        return "receipt/new_receipt";
    }


    @PostMapping("/receipt/add")
    @ResponseBody
    public String addNewReceipt(@RequestBody ReceiptRequest request){

        Receipt receipt = new Receipt();
        receipt.setCreatedAt(new Date());
        receipt.setSupplier(supplierService.getSupplierById(request.getSupplierId()));


        double total = 0d;
        List<ReceiptDetail> receiptDetailList = new ArrayList<>();
        for(ReceiptDetailRequest requestDetail : request.getData()){
            Product product = productService.getProductById(requestDetail.getProductId());
            product.setInStock(product.getInStock() + requestDetail.getQuantity());
            productService.saveProduct(product);

            ReceiptDetail receiptDetail = new ReceiptDetail();
            receiptDetail.setProduct(product);
            receiptDetail.setQuantity(requestDetail.getQuantity());
            receiptDetail.setUnitPrice(requestDetail.getUnitPrice());
            total += receiptDetail.getUnitPrice() * receiptDetail.getQuantity();

            receiptDetailList.add(receiptDetail);
        }
        receipt.setTotalPrice(total);
        Receipt savedReceipt = receiptService.save(receipt);
        for(ReceiptDetail receiptDetail : receiptDetailList){
            receiptDetail.setReceipt(savedReceipt);
        }
        receiptDetailService.saveAll(receiptDetailList);
        return "OK";
    }

    @GetMapping("/receipt/detail/{receiptID}")
    public String getDetailReceipt(@PathVariable("receiptID") Integer receiptID, Model model){
        Receipt receipt = receiptService.getReceiptById(receiptID);
        if(receipt == null){
            return "error/404";
        }

        model.addAttribute("receipt",receipt);
        return "receipt/detail_receipt";
    }

}
