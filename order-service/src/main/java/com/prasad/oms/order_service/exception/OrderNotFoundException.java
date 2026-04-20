package com.prasad.oms.order_service.exception;

import com.prasad.oms.order_service.dto.OrderDTO;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String message){
        super(message);
    }
}
