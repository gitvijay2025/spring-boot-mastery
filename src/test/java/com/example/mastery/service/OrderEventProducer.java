package com.example.mastery.service;

import com.example.mastery.dto.OrderEvent;
import com.example.mastery.entity.Order;
import com.example.mastery.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

//    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;

    public void publishOrderCreated(Long orderId, String status, Long userId, String idempotencyKey) {


        OrderEvent kafkaData = new OrderEvent(orderId, status, userId, idempotencyKey);
        kafkaTemplate.send("order-created-topic", idempotencyKey, kafkaData);
        System.out.println("Create Order Topic Published: " + kafkaData);

//        Map<OrderEvent> kafkaData = new HashMap<>();
//        kafkaData.put("order_id", orderId);
//        kafkaData.put("status", status);
//        kafkaData.put("userId", userId);
//        kafkaData.put("idempotencyKey", idempotencyKey);
//
//        String jsonMessage = objectMapper.writeValueAsString(kafkaData);
//        kafkaTemplate.send("order-created-topic", idempotencyKey, jsonMessage);
//        System.out.println("Create Order Topic Published: " + jsonMessage);
    }
}
