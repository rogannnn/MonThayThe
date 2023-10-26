package com.example.tttndemo.controller;




import com.example.tttndemo.entity.*;
import com.example.tttndemo.exception.ProductNotFoundException;
import com.example.tttndemo.service.*;
import com.example.tttndemo.utils.TopSellingProduct;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    private final StorageService storageService;

    private final ImageProductService imageProductService;

    public ProductController(ProductService productService, ReviewService reviewService, CategoryService categoryService, BrandService brandService, StorageService storageService, ImageProductService imageProductService) {
        this.productService = productService;
        this.reviewService = reviewService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.storageService = storageService;
        this.imageProductService = imageProductService;
    }

    @GetMapping(value = {"/"})
    public String getAll(Model model){
        List<Product> productList = productService.getAll();
        List<Product> topSellingProducts = productService.getTopSellingProduct(20);

        model.addAttribute("listProduct",productList);
        model.addAttribute("topSellingProducts",topSellingProducts);
        return "index";
    }


    @GetMapping("/product/{id}")
    public String viewProductDetail(@PathVariable("id") Integer id, Model model){
        Product product = productService.getProductById(id);
        List<Review> reviewList = reviewService.getListReviewByProduct(product);
        List<Product> listProduct = productService.getTopSellingProduct(4);
        int totalComment = 0;
        double averageRating = 0;
        if(reviewList.size() > 0){
            for(Review r : reviewList){
                averageRating += r.getVote();
                totalComment++;
            }
            averageRating = (double) Math.round((averageRating /  totalComment)*10) / 10;
        }
        model.addAttribute("totalComment", totalComment);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("product", product);
        model.addAttribute("reviewList",reviewList);
        model.addAttribute("listProduct", listProduct);
        return "product-detail";
    }


    @GetMapping("/products")
    public String viewProductDetai1l(@RequestParam(value = "pageNum",required = false) Integer pageNum
            , @RequestParam(value = "sortField",required = false) String sortField
            , @RequestParam(value = "sortDir",required = false) String sortDir
            , @RequestParam(value = "category",required = false) Integer categoryId
            , @RequestParam(value = "brand",required = false) Integer brandId
            , @RequestParam(value = "keyword",required = false) String keyword, Model model){
//        List<Product> productList = productService.getAll();

        List<Category> categoryList = categoryService.listAll();

        List<Brand> brandList = brandService.listAll();

        Page<Product> pageProduct;
        if(pageNum == null){
            pageProduct = productService.findAllPageByKeyword(1, keyword, sortField, sortDir, categoryId , brandId);

        }else {
            pageProduct = productService.findAllPageByKeyword(pageNum,keyword,sortField,sortDir, categoryId, brandId);
        }
        int totalPage = pageProduct.getTotalPages();
        long totalProduct = pageProduct.getTotalElements();
        List<Product> listProducts = pageProduct.getContent();
//        List<Category> listCategories = categoryService.findAll();

        model.addAttribute("listProducts",listProducts);
//        model.addAttribute("listCategories",listCategories);
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("totalProduct",totalProduct);
        if(pageNum == null){
            model.addAttribute("currentPage",1);
        }else {
            model.addAttribute("currentPage",pageNum);
        }
        model.addAttribute("pageTitle","Products-Page "+pageNum);

        model.addAttribute("listProduct",listProducts);
        model.addAttribute("listCategory",categoryList);
        model.addAttribute("listBrand",brandList);
        return "products";
    }

    @GetMapping("/admin/product")
    public String listFirstPage() {
        return "redirect:/admin/product/page/1";
    }

    @GetMapping("/admin/product/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") Integer pageNum, Model model,
                             @RequestParam(defaultValue = "") String keyword,
                             @RequestParam(defaultValue = "id") String sortField,
                             @RequestParam(required = false) String sortDir) {

        model.addAttribute("sortField", sortField);

        if(sortDir == null) sortDir = "asc";
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);

        model.addAttribute("keyword", keyword);


        Page<Product> page = productService.listByPage(pageNum, keyword, sortField, sortDir);
        List<Product> listProducts = page.getContent();
        long startCount = (pageNum - 1) * productService.PRODUCT_PER_PAGE + 1;
        long endCount = startCount +  productService.PRODUCT_PER_PAGE - 1;

        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }



        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);

        model.addAttribute("listProducts", listProducts);


        return "product/products";
    }

    @GetMapping("/admin/product/add")
    public String addProduct(Model model) {
        List<Brand> listBrands = brandService.listAll();
        List<Category> listCategories = categoryService.listAll();

        Product product = new Product();
        product.setEnabled(true);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("listCategories", listCategories);
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", product);
        }

        return "product/new_product";
    }

    @PostMapping("/admin/product/add")
    public String saveProduct(Product product,  RedirectAttributes redirectAttributes, @RequestParam("images") List<MultipartFile> files) throws IOException {


        product.setSoldQuantity(0);
        product.setCreatedAt(new Date());

        Product saveProduct =  productService.saveProduct(product);

        List<ImageProduct> imageProductList = new ArrayList<>();
        for(MultipartFile file : files){
            String path = storageService.upload(file);
            ImageProduct imageProduct = new ImageProduct(path, saveProduct);
            imageProductList.add(imageProduct);
        }
        imageProductService.saveAll(imageProductList);

        redirectAttributes.addFlashAttribute("messageSuccess", "The product has been saved successfully.");
        return "redirect:/admin/product/page/1";



    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            imageProductService.deleteByProductId(id);
            redirectAttributes.addFlashAttribute("messageSuccess", "The product ID " + id + " has been deleted successfully");
        }
        catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/admin/product/page/1";
    }


    @GetMapping("/admin/product/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes, Model model) {
        List<Brand> listBrands = brandService.listAll();
        List<Category> listCategories = categoryService.listAll();
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("listCategories", listCategories);
        if (!model.containsAttribute("product")) {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
        }
        return "product/new_product";
    }

    @PostMapping("/admin/product/edit/{id}")
    public String saveEditProduct(Product product, RedirectAttributes redirectAttributes,
                                  @PathVariable("id") Integer id, @RequestParam(value = "images",required = false) List<MultipartFile> files) {

        try {
            Product existProduct = productService.getProductById(id);
            List<ImageProduct> imageProductList = new ArrayList<>();
//            System.out.println(files.isEmpty());
//            System.out.println(files == null); // dù k chọn file nhưng vẫn ra fasle
//            lỗi chỗ này
            if(files == null && !files.isEmpty()) {
                for(MultipartFile file : files){
                    String url = storageService.upload(file);
                    ImageProduct imageProduct = new ImageProduct(url, product);
                    imageProductList.add(imageProduct);
                }
            }

            imageProductService.saveAll(imageProductList);
            product.setImageProducts(imageProductList);
            product.setSoldQuantity(existProduct.getSoldQuantity());
            product.setCreatedAt(existProduct.getCreatedAt());

            productService.saveProduct(product);

            redirectAttributes.addFlashAttribute("messageSuccess", "The product has been edited successfully.");
            return "redirect:/admin/product/page/1";

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("messageError","Vô dây rồi");
            return "redirect:/admin/product/page/1";
        }
    }


}
