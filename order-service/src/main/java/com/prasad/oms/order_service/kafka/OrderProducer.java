package com.prasad.oms.order_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prasad.oms.order_service.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private static final String TOPIC = "order-created";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendOrderEvent(OrderDTO orderDTO) {
        try {
            String message = objectMapper.writeValueAsString(orderDTO);
            kafkaTemplate.send(TOPIC, message);
            System.out.println("Kafka Event Sent: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}