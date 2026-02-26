	package com.ecommerce.userservice.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.userservice.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	 // 404 - Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(
            ResourceNotFoundException ex) {

        ApiResponse<Object> response =
                new ApiResponse<>("ERROR", ex.getMessage(), null);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 409 - Duplicate Resource
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicate(
            DuplicateResourceException ex) {

        ApiResponse<Object> response =
                new ApiResponse<>("ERROR", ex.getMessage(), null);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // 401 - Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorized(
            UnauthorizedException ex) {

        ApiResponse<Object> response =
                new ApiResponse<>("ERROR", ex.getMessage(), null);

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 500 - Generic Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(
            Exception ex) {

        ApiResponse<Object> response =
                new ApiResponse<>("ERROR", "Something went wrong", null);

        return new ResponseEntity<>(response,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(),
                                   error.getDefaultMessage()));

        ApiResponse<Object> response =
                new ApiResponse<>("ERROR",
                                  "Validation failed",
                                  errors);

        return new ResponseEntity<>(response,
                                    HttpStatus.BAD_REQUEST);
    }
}

