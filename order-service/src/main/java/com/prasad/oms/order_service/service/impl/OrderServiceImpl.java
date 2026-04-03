package com.prasad.oms.order_service.service.impl;


import com.prasad.oms.order_service.client.ProductClient;
import com.prasad.oms.order_service.dto.OrderDTO;
import com.prasad.oms.order_service.dto.OrderEvent;
import com.prasad.oms.order_service.dto.ProductResponse;
import com.prasad.oms.order_service.entity.Order;
import com.prasad.oms.order_service.repository.OrderRepository;
import com.prasad.oms.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;
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


        orderProducer.sendOrderEvent(orderDTO);

        return orderDTO;
    }

    @Override
    public OrderDTO cancelOrder(Long orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found, Please check order Id again"));

        if("CANCELLED".equals(order.getStatus())){
            throw new RuntimeException("Order is already cancelled");
        }

        if("DELIVERDED".equals(order.getStatus())){
            throw new RuntimeException("Order is Delivered, Please contact our customer care support");
        }

        productClient.increaseStock(order.getProductId(), order.getQuantity());

        order.setStatus("CANCELLED");

        Order saved = repository.save(order);

        OrderEvent event = new OrderEvent(
                saved.getUserId(),
                saved.getProductId(),
                saved.getQuantity(),
                saved.getTotalPrice()
        );

        kafkaTemplate.send("Order cancelled successfully, Refund will be credited in your bank accout within 48 hours", event);

        OrderDTO dto = new OrderDTO();
        dto.setId(saved.getId());
        dto.setUserId(saved.getUserId());
        dto.setProductId(saved.getProductId());
        dto.setQuantity(saved.getQuantity());
        dto.setTotalPrice(saved.getTotalPrice());
        dto.setStatus(saved.getStatus());
        return dto;



    }
}
