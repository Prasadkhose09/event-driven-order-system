package com.prasad.oms.order_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prasad.oms.order_service.dto.OrderDTO;
import com.prasad.oms.order_service.dto.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private static final String ORDER_CREATED_TOPIC = "order-created";
    private static final String ORDER_CANCELLED_TOPIC = "order-cancelled";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate; // ✅ Changed String to Object

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendOrderEvent(OrderDTO orderDTO) {
        try {
            String message = objectMapper.writeValueAsString(orderDTO);
            kafkaTemplate.send(ORDER_CREATED_TOPIC, message);
            System.out.println("Kafka Event Sent: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCancelEvent(String key, OrderEvent event) {
        kafkaTemplate.send(ORDER_CANCELLED_TOPIC, key, event); // ✅ Removed saved.getId() — use the key param
        System.out.println("Kafka Cancel Event Sent for key: " + key);
    }
}