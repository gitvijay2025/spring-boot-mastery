package com.example.mastery.service;


import com.example.mastery.dto.UserDtos;
import com.example.mastery.dto.UserDtos.CreateUserRequest;
import com.example.mastery.dto.UserDtos.UserResponse;
import com.example.mastery.entity.User;
import com.example.mastery.repository.UserRepository;


import com.example.mastery.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Service
@RequiredArgsConstructor   // Lombok generates the constructor for all `final` fields below
                               // -- this IS the constructor-injection pattern, just with no
                                // boilerplate. @Autowired isn't needed: single constructor,
                                // Spring auto-detects it.
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService ;

    public UserResponse createUser(CreateUserRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An account already exists for email: " + email
            );
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(email);

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setBalance(request.balance() == null ? 0.0 : request.balance());
        return UserResponse.from(userRepository.save(user));
    }

    public Map<String, Object> login(String email, String password) {
        var result = new HashMap<String, Object>();

        Optional<User> userOpt = userRepository.findByEmail(email);   // ✅ sirf email se dhoondo

        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            // ✅ matches() use kiya — plain password vs DB ka hashed password
            var token = jwtService.generateToken(email);
            result.put("token", token);
            result.put("status", true);
            result.put("message", "Login Success");
        } else {
            result.put("status", false);
            result.put("message", "Login failed");
        }

        return result;
    }

    @Cacheable(value = "users-v3", key = "#id")  // ✅ "users-v2" = cache ka naam, key = cache key
    public UserResponse getUser(Long id) {
        System.out.println("DB CALL HO RAHI HAI");

        return userRepository.findById(id)
                .map(UserResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with id: " + id));
    }

    @CacheEvict(value = "users-v2", key = "#id")   // ✅ update hote hi cache se hata do
    public UserResponse updateUser(Long id, String newName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id: " + id
                ));
        user.setName(newName);
        return UserResponse.from(userRepository.save(user));
    }


    public Page<UserResponse> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponse::from);
    }

    @Transactional
    public UserResponse withdraw(Long id, Double amount) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Invalid User id: " + id));

        if (user.getBalance() < amount) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Insufficient balance, actual Balance is: " + user.getBalance()
            );
        }

        user.setBalance(user.getBalance() - amount);
        User saved = userRepository.save(user);   // ✅ explicitly SAVE karo — persist ho

        return UserResponse.from(saved);           // ✅ return karo
    }




}
