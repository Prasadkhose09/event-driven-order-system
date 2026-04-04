package com.prasad.oms.notification_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prasad.oms.notification_service.dto.OrderEvent;
import com.prasad.oms.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = {"order-created", "order-cancelled"},
            groupId = "notification-group"
    )
    public void consume(String message,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        try {
            if (message == null || message.isBlank()) {
                log.warn("⚠️ Empty message received");
                return;
            }

            OrderEvent order = objectMapper.readValue(message, OrderEvent.class);

            log.info("Event received from topic {}: {}", topic, order);

            // 👉 Decide action based on topic
            if ("order-created".equals(topic)) {
                notificationService.sendOrderPlacedEmail(order);
            } else if ("order-cancelled".equals(topic)) {
                notificationService.sendOrderCancelledEmail(order);
            }

        } catch (Exception e) {
            log.error("Failed to process message from topic {}", topic, e);
        }
    }
}