package com.shulventures.solarservicesbackend.controller;

import com.shulventures.solarservicesbackend.dto.LoginRequest;
import com.shulventures.solarservicesbackend.dto.LoginResponse;
import com.shulventures.solarservicesbackend.entity.User;
import com.shulventures.solarservicesbackend.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest
    ) {

        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElse(null);

        // User not found
        if (user == null) {

            LoginResponse response = new LoginResponse(
                    false,
                    "Invalid email or password",
                    null,
                    null,
                    null
            );

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        // Password doesn't match
        if (!user.getPassword().equals(loginRequest.getPassword())) {

            LoginResponse response = new LoginResponse(
                    false,
                    "Invalid email or password",
                    null,
                    null,
                    null
            );

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        // Login successful
        LoginResponse response = new LoginResponse(
                true,
                "Login successful",
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return ResponseEntity.ok(response);
    }
}