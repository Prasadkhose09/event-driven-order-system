package com.prasad.oms.order_service.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private int quantity;
    private double totalPrice;
}
