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
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendOrderEvent(OrderDTO orderDTO) {
        try {
            String json = objectMapper.writeValueAsString(orderDTO);

            kafkaTemplate.send("order-created", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCancelEvent(String key, OrderEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("order-cancelled", key, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}