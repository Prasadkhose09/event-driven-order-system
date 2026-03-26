package com.prasad.oms.order_service.service.impl;


import com.prasad.oms.order_service.client.ProductClient;
import com.prasad.oms.order_service.dto.OrderDTO;
import com.prasad.oms.order_service.dto.ProductResponse;
import com.prasad.oms.order_service.entity.Order;
import com.prasad.oms.order_service.repository.OrderRepository;
import com.prasad.oms.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.prasad.oms.order_service.kafka.OrderProducer;
@Service
public class OrderServiceImpl  implements OrderService {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderProducer orderProducer;


    @Autowired
    private ProductClient productClient;

    @Override
    public OrderDTO placeOrder(OrderDTO orderDTO){

        ProductResponse product = productClient.getProductById(orderDTO.getProductId());

        if(product == null){
            throw new RuntimeException("Product not found");
        }

        if(product.getStock() < orderDTO.getQuantity()){
            throw new RuntimeException("Insufficient stock");
        }

        double totalPrice = product.getPrice() * orderDTO.getQuantity();

        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setProductId(orderDTO.getProductId());
        order.setQuantity(orderDTO.getQuantity());
        order.setTotalPrice(totalPrice);

        Order saved = repository.save(order);

        orderDTO.setId(saved.getId());
        orderDTO.setTotalPrice(totalPrice);

        // 🔥 SEND EVENT TO KAFKA
        orderProducer.sendOrderEvent(orderDTO);

        return orderDTO;
    }
}
