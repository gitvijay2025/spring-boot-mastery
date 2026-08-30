package com.example.mastery.service;

import com.example.mastery.dto.OrderDtos.OrderResponse;
import com.example.mastery.dto.OrderDtos.OrderRequest;
import com.example.mastery.entity.Order;
import com.example.mastery.entity.User;
import com.example.mastery.kafka.OrderEventProducer;
import com.example.mastery.repository.OrderRepository;
import com.example.mastery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository ;
    private final UserRepository userRepository ;
    private final OrderEventProducer orderEventProducer;
    private final EmailService emailService;

    public OrderResponse createOrder(OrderRequest order){
        String idempotencyKey = order.idempotencyKey();

        User user = userRepository.findById(order.userId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with ID: " + order.userId()
                ));

        if (orderRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An order already exists with idempotency key: " + idempotencyKey
            );
        }

        Order newOrder = new Order();
        newOrder.setAmount(order.amount());
        newOrder.setUser(user);
        newOrder.setIdempotencyKey(idempotencyKey);
        newOrder.setStatus(Order.OrderStatus.CONFIRMED);


        OrderResponse ordRes = OrderResponse.from(orderRepository.save(newOrder));


        emailService.sendEmailtoCustomer(ordRes.id(), user.getEmail());

        orderEventProducer.publishOrderCreated( ordRes.id(),  ordRes.status().toString(), ordRes.userId(), ordRes.idempotencyKey());

        return ordRes;
    }



}
