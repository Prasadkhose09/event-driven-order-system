package com.prasad.oms.order_service.exception;

public class DeliveredOrderException extends RuntimeException{
    public DeliveredOrderException(String message){
        super(message);
    }
}
