package com.example.mastery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UserDtos {

    public record CreateUserRequest(
         @NotBlank   (message = "Name is required.") String name,
         @Email (message = "Email must be valid") @NotBlank String email,


         @NotBlank(message = "Password is required")
         @Size(min = 8, message = "Password must be at least 8 characters")
         String password,

         @PositiveOrZero(message = "Balance must be zero or greater.") Double balance
    ) {}

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ) {}

    // ----- Response DTO -----
    public record UserResponse(
            Long id,
            String name,
            String email,
            Double balance,
            LocalDateTime createdAt
    ) {
        // Records can still have static factory methods -- common pattern
        // to keep the Entity -> DTO mapping colocated with the DTO itself.
        public static UserResponse from(com.example.mastery.entity.User user) {
            return new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getBalance(),
                    user.getCreatedAt()
            );
        }
    }

    // ----- Withdraw request -----
    public record WithdrawRequest(Long userId, Double amount) {}
}
