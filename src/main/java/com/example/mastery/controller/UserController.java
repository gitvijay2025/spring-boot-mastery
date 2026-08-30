package com.example.mastery.controller;


import com.example.mastery.dto.UserDtos;
import com.example.mastery.dto.UserDtos.CreateUserRequest;
import com.example.mastery.dto.UserDtos.UserResponse;

import com.example.mastery.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse>  createUser(@Valid @RequestBody CreateUserRequest request){

        return ResponseEntity.ok(userService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserDtos.LoginRequest request) {
        return ResponseEntity.ok(userService.login(request.email(), request.password()));
    }




    @GetMapping("/{id}")
    public ResponseEntity<UserResponse>  getUser(@PathVariable Long id ) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestParam String name
    ) {
        return ResponseEntity.ok(userService.updateUser(id, name));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(userService.getUsers(pageable));
    }


    @PostMapping("/{id}/withdraw")
    public ResponseEntity<UserResponse> withdraw (@PathVariable Long id , @RequestParam Double amount){
        return  ResponseEntity.ok(userService.withdraw (id, amount));
    }



}
