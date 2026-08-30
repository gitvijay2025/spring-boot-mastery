package com.example.mastery.dto;

public record OrderEvent(Long orderId, String status, Long userId, String idempotencyKey) {}