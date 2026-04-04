package com.prasad.oms.order_service.service.impl;

import com.prasad.oms.order_service.client.ProductClient;
import com.prasad.oms.order_service.dto.OrderDTO;
import com.prasad.oms.order_service.dto.OrderEvent;
import com.prasad.oms.order_service.dto.ProductResponse;
import com.prasad.oms.order_service.entity.Order;
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

        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setProductId(orderDTO.getProductId());
        order.setQuantity(orderDTO.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setStatus("CREATED"); // ✅ IMPORTANT

        Order saved = repository.save(order);

        log.info("✅ Order saved with ID={}", saved.getId());

        orderDTO.setId(saved.getId());
        orderDTO.setTotalPrice(totalPrice);
        orderDTO.setStatus(saved.getStatus());

        // ✅ Send Kafka event
        orderProducer.sendOrderEvent(orderDTO);

        return orderDTO;
    }

    @Override
    public OrderDTO cancelOrder(Long orderId) {

        log.info("Cancelling order ID={}", orderId);

        Order order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // ✅ Better status handling
        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Order already cancelled");
        }

        if ("DELIVERED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Delivered order cannot be cancelled");
        }

        // ✅ Restore stock
        productClient.increaseStock(order.getProductId(), order.getQuantity());
        log.info("🔄 Stock restored for productId={}", order.getProductId());

        // ✅ Update status
        order.setStatus("CANCELLED");

        Order saved = repository.save(order);

        log.info("Order cancelled successfully ID={}", saved.getId());

        // ✅ Create event
        OrderEvent event = new OrderEvent(
                saved.getUserId(),
                saved.getProductId(),
                saved.getQuantity(),
                saved.getTotalPrice()
        );

        // ✅ Send cancel event to Kafka
        orderProducer.sendCancelEvent(String.valueOf(saved.getId()), event);

        // ✅ Convert to DTO
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