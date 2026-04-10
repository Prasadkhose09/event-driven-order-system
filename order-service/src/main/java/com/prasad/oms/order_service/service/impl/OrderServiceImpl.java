package com.prasad.oms.order_service.service.impl;

import com.prasad.oms.order_service.client.ProductClient;
import com.prasad.oms.order_service.dto.OrderDTO;
import com.prasad.oms.order_service.dto.OrderEvent;
import com.prasad.oms.order_service.dto.ProductResponse;
import com.prasad.oms.order_service.entity.Order;
import com.prasad.oms.order_service.mapper.OrderMapper;
import com.prasad.oms.order_service.repository.OrderRepository;
import com.prasad.oms.order_service.service.OrderService;
import com.prasad.oms.order_service.kafka.OrderProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderProducer orderProducer;
    private final ProductClient productClient;

    private final OrderMapper mapper;

    @Override
    public OrderDTO placeOrder(OrderDTO orderDTO) {

        log.info("🛒 Placing order for productId={}", orderDTO.getProductId());

        ProductResponse product = productClient.getProductById(orderDTO.getProductId());

        if (product == null) {
            log.error("Product not found");
            throw new RuntimeException("Product not found");
        }

        if (product.getStock() < orderDTO.getQuantity()) {
            log.error("Insufficient stock");
            throw new RuntimeException("Insufficient stock");
        }

        double totalPrice = product.getPrice() * orderDTO.getQuantity();

        Order order = mapper.toEntity(orderDTO);
        order.setTotalPrice(totalPrice);
        order.setStatus("CREATED");
        orderDTO.setTotalPrice(totalPrice);
        order.setEmail(orderDTO.getEmail()); // ✅ fix 1

        Order saved = repository.save(order);

        log.info("✅ Order saved with ID={}", saved.getId());

        orderProducer.sendOrderEvent(orderDTO); // ✅ fix 2

        return mapper.toDTO(saved);
    }

    @Override
    public OrderDTO cancelOrder(Long orderId) {

        log.info("Cancelling order ID={}", orderId);

        Order order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Order already cancelled");
        }

        if ("DELIVERED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Delivered order cannot be cancelled");
        }

        productClient.increaseStock(order.getProductId(), order.getQuantity());
        log.info("🔄 Stock restored for productId={}", order.getProductId());

        order.setStatus("CANCELLED");

        Order saved = repository.save(order);

        log.info("Order cancelled successfully ID={}", saved.getId());

        OrderEvent event = new OrderEvent(
                saved.getUserId(),
                saved.getProductId(),
                saved.getQuantity(),
                saved.getTotalPrice(),
                saved.getEmail() // ✅ fix 3
        );

        orderProducer.sendCancelEvent(String.valueOf(saved.getId()), event);

        return mapper.toDTO(saved); // ✅ fix 4 - removed unused dto
    }
}