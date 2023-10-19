package com.example.mon_thay_the.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.mon_thay_the.error.Error.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BrandRequest {
    @NotBlank(message = NAME_BLANK_ERROR)
    @Size(max = 100, message = "Name should be less than 100 characters")
    private String name;

    @NotNull(message = "Enabled must not be null")
    private Boolean enabled;
    

    @NotBlank(message = "Description must not be blank")
    private String description;
}
