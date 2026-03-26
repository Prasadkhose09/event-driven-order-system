package com.prasad.oms.order_service.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    // Optional config (Spring Boot auto-config handles most)
}