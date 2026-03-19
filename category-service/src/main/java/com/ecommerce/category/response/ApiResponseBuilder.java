package com.ecommerce.category.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ApiResponseBuilder {

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data, HttpStatus status) {

        ApiResponse<T> response = new ApiResponse<>(
                true,
                message,
                data,
                status.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status) {

        ApiResponse<T> response = new ApiResponse<>(
                false,
                message,
                null,
                status.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, status);
    }
}