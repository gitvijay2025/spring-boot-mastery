package com.example.mastery.controller;


import com.example.mastery.dto.OrderDtos;
import com.example.mastery.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDtos.OrderResponse> createOrder(@Valid @RequestBody OrderDtos.OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }


}
