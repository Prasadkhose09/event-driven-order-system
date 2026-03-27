package com.prasad.oms.notification_service.service;

import com.prasad.oms.notification_service.dto.OrderEvent;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(OrderEvent order) {

        System.out.println("📩 Sending notification...");
        System.out.println("User " + order.getUserId() +
                " placed order worth ₹" + order.getTotalPrice());

        // simulate delay / processing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("✅ Notification Sent Successfully!");
    }
}