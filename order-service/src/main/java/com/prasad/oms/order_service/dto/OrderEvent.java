package com.prasad.oms.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {

    private Long userId;
    private Long productId;
    private int quantity;
    private double totalPrice;
}