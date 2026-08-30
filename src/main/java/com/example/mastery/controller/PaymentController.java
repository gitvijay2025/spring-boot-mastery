package com.example.mastery.controller;

import com.example.mastery.dto.PaymentDto;
import com.example.mastery.service.PaymentClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentClient paymentClient;

    @PostMapping
    public ResponseEntity<?> processPayment(@Valid @RequestBody PaymentDto request ){
        return ResponseEntity.ok(paymentClient.processPayment(request));
    }
}
