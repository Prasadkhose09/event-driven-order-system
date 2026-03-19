package com.prasad.oms.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 1, message = "Price must be greater than 0")
    private double price;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;
}
