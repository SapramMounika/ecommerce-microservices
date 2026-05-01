package com.ecommerce.order.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseBuilder {

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data, HttpStatus status) {

        ApiResponse<T> response = new ApiResponse<T>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setStatus(status.value());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status) {

        ApiResponse<T> response = new ApiResponse<T>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        response.setStatus(status.value());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, status);
    }
}