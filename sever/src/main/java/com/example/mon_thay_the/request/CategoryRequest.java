package com.example.mon_thay_the.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CategoryRequest {

    @NotBlank(message = "Name must not be blank")
    @Size(max = 128, message = "Name should be less than 128 characters")
    private String name;

    @NotNull(message = "Enabled must not be null")
    private Boolean enabled;

    @NotBlank(message = "Image must not be blank")
    private String image;

    @NotBlank(message = "Description must not be blank")
    private String description;
}
