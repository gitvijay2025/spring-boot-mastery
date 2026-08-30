package com.example.mastery.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Async
    public void sendEmailtoCustomer (Long orderId , String username ){
        System.out.println(Thread.currentThread().getName());
        try{
            Thread.sleep(10000);
            System.out.println("Email order is Sent >>> Order ID: "+ orderId + " username :" + username);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // throw new RuntimeException(e);
        }
    }

    @Async
    public void sendEmailtoCustomerCompletable  (Long orderId , String username ){
        System.out.println(Thread.currentThread().getName());
        try{
            Thread.sleep(10000);
            System.out.println("Email order is Sent >>> Order ID: "+ orderId + " username :" + username);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // throw new RuntimeException(e);
        }
    }


}
