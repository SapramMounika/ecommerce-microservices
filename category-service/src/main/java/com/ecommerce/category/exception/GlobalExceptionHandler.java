package com.ecommerce.category.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.category.response.ApiResponse;
import com.ecommerce.category.response.ApiResponseBuilder;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔴 Category Not Found
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(
            CategoryNotFoundException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    // 🔴 Category Already Exists
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicate(
            CategoryAlreadyExistsException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.CONFLICT
        );
    }

    // 🔴 Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ApiResponseBuilder.success(
                "Validation failed",
                errors,
                HttpStatus.BAD_REQUEST
        );
    }

    // 🔴 Illegal Argument (Optional)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    // 🔴 Generic Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {

        return ApiResponseBuilder.error(
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}