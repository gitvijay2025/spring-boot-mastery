package com.example.mastery.kafka;


import com.example.mastery.dto.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Kafka guarantees AT-LEAST-ONCE delivery, never exactly-once by default.
 * That means this consumer WILL occasionally see the same message twice
 * (e.g. if it crashes after processing but before the offset commit).
 * So processing must be idempotent -- we track processed event IDs and
 * skip anything we've already handled, rather than trying to prevent
 * duplicates at the producer side (which isn't fully possible anyway).
 */
@Service
public class OrderEventConsumer {

    // In a real app this would be a DB table with a UNIQUE constraint on
    // eventId (race-condition safe), not an in-memory Set.
    private final Set<String> processedEvents = ConcurrentHashMap.newKeySet();

    @KafkaListener(topics = "order-created-topic", groupId = "mastery-group")
    public void handleOrderCreated(OrderEvent event) {
        String eventId = event.idempotencyKey();   // ✅ record ka field seedha accessor se milta hai

        System.out.println("Check Event ID " + eventId);
        if (true) {
            System.err.println("Simulating a processing failure for DLQ test");
            throw new RuntimeException("Simulating a processing failure for DLQ test");
        }
        if (!processedEvents.add(eventId)) {
            return;
        }

        System.out.println("Processing order event: " + event);
    }


//    @KafkaListener(
//            topics = "order-created-topic.DLT",
//            groupId = "dlq-test-group",
//            properties = "value.deserializer=org.apache.kafka.common.serialization.StringDeserializer"
//    )
    @KafkaListener(
            topics = "order-created-topic-dlt",
            groupId = "dlq-test-group"

    )
    public void handleDeadLetter(OrderEvent event) {
        System.out.println("DLQ MEIN MESSAGE AAYA: " );
    }

    private String extractEventId(String message) {
        return message; // placeholder -- real code parses JSON and pulls an id field
    }
}
