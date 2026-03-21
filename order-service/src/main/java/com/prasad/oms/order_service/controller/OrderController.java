package com.prasad.oms.order_service.controller;

import com.prasad.oms.order_service.dto.OrderDTO;
import com.prasad.oms.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {


    @Autowired
    private OrderService service;

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok(service.placeOrder(orderDTO));
    }
}
