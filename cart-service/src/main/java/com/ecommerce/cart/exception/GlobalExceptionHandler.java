package com.ecommerce.cart.exception;

import com.ecommerce.cart.response.ApiResponse;
import com.ecommerce.cart.response.ApiResponseBuilder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =====================================================
    // 🔴 404 - CART NOT FOUND
    // =====================================================
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleCartNotFound(CartNotFoundException ex) {

        // Using builder → automatically sets timestamp
        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    // =====================================================
    // 🔴 404 - CART ITEM NOT FOUND
    // =====================================================
    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleCartItemNotFound(CartItemNotFoundException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    // =====================================================
    // 🔴 409 - ITEM ALREADY EXISTS IN CART
    // =====================================================
    @ExceptionHandler(CartItemAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleItemAlreadyExists(CartItemAlreadyExistsException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.CONFLICT
        );
    }

    // =====================================================
    // 🔴 400 - CART EMPTY (BUSINESS VALIDATION)
    // =====================================================
    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<ApiResponse<Object>> handleCartEmpty(CartEmptyException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    // =====================================================
    // 🔴 400 - VALIDATION ERROR (@Valid)
    // =====================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {

        // Collect all field errors
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        // Build custom response (because we want to send error map)
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Validation failed",
                errors,
                HttpStatus.BAD_REQUEST.value(),
                java.time.LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // =====================================================
    // 🔴 500 - GENERIC EXCEPTION (FALLBACK)
    // =====================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {

        // Optional: log error
        ex.printStackTrace();

        return ApiResponseBuilder.error(
                "Something went wrong. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}