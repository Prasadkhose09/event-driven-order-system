package com.prasad.oms.product_service.exception;

import com.prasad.oms.product_service.common.ApiResponse;
import com.prasad.oms.product_service.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex){
        return buildResponse(
                ex.getMessage(),
                "PRODUCT_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }

    private ResponseEntity<Object> buildResponse(String message, String code, HttpStatus status){
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                null,
                new ErrorResponse(code, message)
        );

        return new ResponseEntity<>(response, status);
    }
}