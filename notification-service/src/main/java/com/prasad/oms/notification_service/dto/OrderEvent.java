package com.prasad.oms.notification_service.dto;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderEvent {

    private Long userId;
    private Long productId;
    private int quantity;
    private double totalPrice;
    private String email;
}