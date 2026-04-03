package com.prasad.oms.order_service.controller;

import com.prasad.oms.order_service.dto.OrderDTO;
import com.prasad.oms.order_service.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {


    @Autowired
    private OrderService service;

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok(service.placeOrder(orderDTO));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long id){
        return ResponseEntity.ok(service.cancelOrder(id));
    }
}
