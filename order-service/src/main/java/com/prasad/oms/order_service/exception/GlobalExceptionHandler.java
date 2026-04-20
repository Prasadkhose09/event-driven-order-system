package com.prasad.oms.order_service.exception;


import com.prasad.oms.order_service.common.ApiResponse;
import com.prasad.oms.order_service.common.ErrorResponse;
import com.prasad.oms.order_service.entity.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponse(String message, String code, HttpStatus status){
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                null,
                new ErrorResponse(code, message)

        );
        return new ResponseEntity<>(response, status);

    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex){
      return buildResponse(ex.getMessage(),"PRODUCT_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Object> handleInsufficientBalance(InsufficientStockException ex){
        return buildResponse(ex.getMessage(),"INSUFFICIENT_STOCK", HttpStatus.INSUFFICIENT_STORAGE);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException ex){
    return buildResponse(ex.getMessage(), "ORDER_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderAlreadyCancelledException.class)
    public ResponseEntity<Object> handleOrderAlreadyCancelledException(OrderAlreadyCancelledException ex){
        return buildResponse(ex.getMessage(), "ORDER_ALREADY_CANCEL", HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(DeliveredOrderException.class)
    public ResponseEntity<Object> handleDeliveredOrderException(DeliveredOrderException ex){
        return buildResponse(ex.getMessage(), "DELIVERED_ORDER_CAN'T_BE_CANCELLED", HttpStatus.NOT_ACCEPTABLE);
    }





}
