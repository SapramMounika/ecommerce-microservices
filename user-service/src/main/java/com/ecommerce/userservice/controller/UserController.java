package com.ecommerce.userservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.userservice.dto.*;
import com.ecommerce.userservice.response.ApiResponse;
import com.ecommerce.userservice.response.ApiResponseBuilder;
import com.ecommerce.userservice.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // VIEW ALL USERS
    @GetMapping("/viewAll")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<UserGetResponse>>> getAllUsers() {

        List<UserGetResponse> users = userService.getAllUsers();

        return ApiResponseBuilder.success(
                "Users fetched successfully",
                users,
                HttpStatus.OK
        );
    }

    // VIEW USER BY ID
    @GetMapping("/viewById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<UserGetResponse>> getUserById(@PathVariable Long id) {

        UserGetResponse user = userService.getUserById(id);

        return ApiResponseBuilder.success(
                "User fetched successfully",
                user,
                HttpStatus.OK
        );
    }

    // CREATE USER
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserCreateResponse>> createUser(
            @Valid @RequestBody UserCreateRequest request) {

        UserCreateResponse response = userService.createUser(request);

        return ApiResponseBuilder.success(
                "User created successfully",
                response,
                HttpStatus.CREATED
        );
    }

    // UPDATE USER
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        UserUpdateResponse response = userService.updateUser(id, request);

        return ApiResponseBuilder.success(
                "User updated successfully",
                response,
                HttpStatus.OK
        );
    }

    // DELETE USER
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDeleteResponse>> deleteUser(@PathVariable Long id) {

        UserDeleteResponse response = userService.deleteUser(id);

        return ApiResponseBuilder.success(
                "User deleted successfully",
                response,
                HttpStatus.OK
        );
    }
}