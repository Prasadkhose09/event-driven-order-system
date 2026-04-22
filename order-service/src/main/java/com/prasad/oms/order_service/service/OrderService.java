package com.prasad.oms.order_service.service;

import com.prasad.oms.order_service.dto.OrderDTO;

public interface OrderService {
    OrderDTO placeOrder(OrderDTO orderDTO);
    OrderDTO cancelOrder(Long orderDTO);
    OrderDTO getOrderById(Long id);


}
