package com.example.mastery.kafka;

import com.example.mastery.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void publishOrderCreated(Long orderId, String status, Long userId, String idempotencyKey) {
        try {
            OrderEvent event = new OrderEvent(orderId, status, userId, idempotencyKey);
            kafkaTemplate.send("order-created-topic", idempotencyKey, event);
            System.out.println("Create Order Topic Published: " + event);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to serialize order event", e);
        }
    }
}
