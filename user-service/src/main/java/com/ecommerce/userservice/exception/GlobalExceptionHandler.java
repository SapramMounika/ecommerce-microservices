package com.ecommerce.userservice.exception;

import com.ecommerce.userservice.response.ApiResponse;
import com.ecommerce.userservice.response.ApiResponseBuilder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;   // ✅ Correct import
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    // 409 - Duplicate Resource
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicate(DuplicateResourceException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.CONFLICT
        );
    }

    // 401 - Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorized(UnauthorizedException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }

    // 400 - Validation Error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Validation failed",
                errors,
                HttpStatus.BAD_REQUEST.value(),
                java.time.LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 403 - Access Denied (Wrong Role)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {

        return ApiResponseBuilder.error(
                "Access denied",
                HttpStatus.FORBIDDEN
        );
    }
    
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidCredentials(
            InvalidCredentialsException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }

    // 500 - Generic Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {

        return ApiResponseBuilder.error(
                "Something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}