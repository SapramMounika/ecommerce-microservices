package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.LoginRequest;
import com.ecommerce.userservice.dto.LoginResponse;
import com.ecommerce.userservice.dto.RegisterRequest;
import com.ecommerce.userservice.dto.RegisterResponse;
import com.ecommerce.userservice.response.ApiResponse;
import com.ecommerce.userservice.response.ApiResponseBuilder;
import com.ecommerce.userservice.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // REGISTER USER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @RequestBody RegisterRequest request) {

        RegisterResponse response = authService.register(request);

        return ApiResponseBuilder.success(
                "User registered successfully",
                response,
                HttpStatus.CREATED
        );
    }

    // LOGIN USER
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ApiResponseBuilder.success(
                "Login successful",
                response,
                HttpStatus.OK
        );
    }
}