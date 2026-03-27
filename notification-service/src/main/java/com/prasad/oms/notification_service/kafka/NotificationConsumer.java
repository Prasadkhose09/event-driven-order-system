package com.prasad.oms.notification_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prasad.oms.notification_service.dto.OrderEvent;
import com.prasad.oms.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @Autowired
    private NotificationService notificationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "order-created", groupId = "notification-group")
    public void consume(String message) {

        try {
            // skip non-JSON messages
            if (!message.startsWith("{")) {
                System.out.println("⚠️ Skipping invalid message: " + message);
                return;
            }

            OrderEvent order = objectMapper.readValue(message, OrderEvent.class);

            System.out.println("📥 Event received in Notification Service");

            notificationService.sendNotification(order);

        } catch (Exception e) {
            System.out.println("❌ Failed to process notification: " + message);
        }
    }
}