package com.example.mastery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import com.example.mastery.entity.Order;
import java.time.LocalDateTime;

public class OrderDtos {

    public record OrderRequest(
            @PositiveOrZero(message = "User ID must be zero or greater.") Long userId,
            @NotBlank(message = "Idempotency key is required.") String idempotencyKey,
            @PositiveOrZero(message = "Amount must be zero or greater.") Double amount
    ) {}

    public record OrderResponse(
            Long id,
            Long userId,
            String idempotencyKey,
            Double amount,
            Order.OrderStatus status,
            LocalDateTime createdAt
    ) {
        public static OrderResponse from(Order order) {
            return new OrderResponse(
                    order.getId(),
                    order.getUser().getId(),
                    order.getIdempotencyKey(),
                    order.getAmount(),
                    order.getStatus(),
                    order.getCreatedAt()
            );
        }
    }
}
