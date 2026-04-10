package com.prasad.oms.notification_service.service;

import com.prasad.oms.notification_service.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;


    // ✅ Order Created
    public void sendOrderPlacedEmail(OrderEvent order) {

        String email = order.getEmail();

        String subject = "Order Placed Successfully 🎉";

        String body = "Your order has been placed successfully.\n\n"
                + "Product ID: " + order.getProductId() + "\n"
                + "Quantity: " + order.getQuantity() + "\n"
                + "Total Price: ₹" + order.getTotalPrice();

        emailService.sendEmail(email, subject, body);

        log.info("📧 Order placed email sent");
    }

    // ✅ Order Cancelled
    public void sendOrderCancelledEmail(OrderEvent order) {

        String email = order.getEmail();

        String subject = "Order Cancelled";

        String body = "Your order has been cancelled.\n\n"
                + "Product ID: " + order.getProductId() + "\n"
                + "Quantity: " + order.getQuantity() + "\n"
                + "Total Price: ₹" + order.getTotalPrice();

        emailService.sendEmail(email, subject, body);

        log.info("📧 Order cancelled email sent");
    }
}