package com.prasad.oms.order_service.service.impl;

import com.prasad.oms.order_service.client.ProductClient;
import com.prasad.oms.order_service.client.UserClient;
import com.prasad.oms.order_service.dto.OrderDTO;
import com.prasad.oms.order_service.dto.OrderEvent;
import com.prasad.oms.order_service.dto.ProductResponse;
import com.prasad.oms.order_service.dto.UserResponse;
import com.prasad.oms.order_service.entity.Order;
import com.prasad.oms.order_service.exception.*;
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
    private final UserClient userClient;



    @Override
    public OrderDTO placeOrder(OrderDTO orderDTO) {

        log.info("Placing order for productId={}", orderDTO.getProductId());

        // ✅ 1. fetch user first
        UserResponse user = userClient.getUserById(orderDTO.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        log.info("Fetched user: {}", user);

        // ✅ 2. validate product
        ProductResponse product = productClient.getProductById(orderDTO.getProductId());
        if (product == null) {
            throw new ProductNotFoundException("Product not found");
        }
        if (product.getStock() < orderDTO.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock");
        }

        // ✅ 3. calculate price
        double totalPrice = product.getPrice() * orderDTO.getQuantity();

        // ✅ 4. build order entity
        Order order = mapper.toEntity(orderDTO);
        order.setTotalPrice(totalPrice);
        order.setStatus("CREATED");
        order.setEmail(user.getEmail()); // from user-service

        // ✅ 5. save to DB
        Order saved = repository.save(order);
        log.info("Order saved with ID={}", saved.getId());

        // ✅ 6. set email on DTO before sending to Kafka
        orderDTO.setEmail(user.getEmail());
        orderDTO.setTotalPrice(totalPrice);
        orderDTO.setId(saved.getId());

        // ✅ 7. send Kafka event
        orderProducer.sendOrderEvent(orderDTO);

        return mapper.toDTO(saved);
    }

    @Override
    public OrderDTO cancelOrder(Long orderId) {

        log.info("Cancelling order ID={}", orderId);

        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            throw new OrderAlreadyCancelledException("Order already cancelled");
        }

        if ("DELIVERED".equalsIgnoreCase(order.getStatus())) {
            throw new DeliveredOrderException("Delivered order cannot be cancelled");
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

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return mapper.toDTO(order);
    }
}