package com.prasad.oms.notification_service.service;

import com.prasad.oms.notification_service.dto.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private EmailService emailService;

    public void sendNotification(OrderEvent order) {

        String to = "chrismarti7889@gmail.com"; // change this
        String subject = "Order Confirmation";

        String body = "Hello,\n\nYour order has been placed successfully.\n\n"
                + "Product ID: " + order.getProductId() + "\n"
                + "Quantity: " + order.getQuantity() + "\n"
                + "Total Amount: ₹" + order.getTotalPrice() + "\n\n"
                + "Thank you!";

        emailService.sendEmail(to, subject, body);
    }
}