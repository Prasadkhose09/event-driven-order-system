package com.prasad.oms.product_service.common;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse <T> {
    private boolean success;
    private T data;
    private ErrorResponse errorResponse;

}
