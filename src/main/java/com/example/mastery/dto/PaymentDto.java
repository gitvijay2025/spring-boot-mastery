package com.example.mastery.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;


public record PaymentDto(

        @NotNull(message = "Order Id is required")
        Long orderId,

        @NotNull(message = "Amount is required")
        @PositiveOrZero(message = "Amount must be zero or a positive value")
        Double amount,

        @NotNull(message = "User ID is required for Payment Process")
        Long userId
) {}
