package com.example.mon_thay_the.request;


import com.example.mon_thay_the.entity.ImageProduct;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductRequest {


    private Integer id;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name should be less than 255 characters")
    private String name;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotNull(message = "Price must not be null")
    @Min(message = "Price should be greater than equal 0", value = 0L)
    private Double price;

    @NotNull(message = "Image must not be null")
    private List<String> images;

    @NotNull(message = "Sold quantity must not be null")
    private Integer soldQuantity;

    @NotNull(message = "In stock must not be null")
    @Min(message = "In stock should be greater than equal 0", value = 0L)
    private Integer inStock;

    @NotNull(message = "Category must not be null")
    private Integer categoryId;

    @NotNull(message = "Brand must not be null")
    private Integer brandId;

    @NotNull(message = "Enabled must not be null")
    private Boolean enabled;

}
