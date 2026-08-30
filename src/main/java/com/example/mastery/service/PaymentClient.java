package com.example.mastery.service;

import com.example.mastery.dto.PaymentDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentClient {

    @CircuitBreaker(name="paymentService", fallbackMethod = "paymentFallback")
    public String processPayment(PaymentDto request) {


        int min = 0;
        int max = 1;
        int randomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);
        if (randomNumber == 1) {
            return  "Payment Process Details "+request.userId() + " " + request.orderId() + " " + request.amount();
        } else {
            throw new RuntimeException("not working");
        }
    }

    public String paymentFallback(PaymentDto request, Throwable t) {
        return "Payment service unavailable, please try again later. Reason: "+ t.getMessage() ;
    }
}
