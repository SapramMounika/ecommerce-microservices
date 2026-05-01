package com.ecommerce.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.order.response.ApiResponse;
import com.ecommerce.order.response.ApiResponseBuilder;

@RestControllerAdvice  // ✅ VERY IMPORTANT
public class GlobalExceptionHandler {

    // ===============================
    // CUSTOM BUSINESS EXCEPTION
    // ===============================
    @ExceptionHandler(OrderServiceException.class)
    public ResponseEntity<ApiResponse<Object>> handleOrderServiceException(OrderServiceException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.valueOf(ex.getStatus())
        );
    }

    // ===============================
    // CART EMPTY
    // ===============================
    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<ApiResponse<Object>> handleCartEmpty(CartEmptyException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    // ===============================
    // PRODUCT NOT FOUND
    // ===============================
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleProductNotFound(ProductNotFoundException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    // ===============================
    // OUT OF STOCK
    // ===============================
    @ExceptionHandler(ProductOutOfStockException.class)
    public ResponseEntity<ApiResponse<Object>> handleOutOfStock(ProductOutOfStockException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    // ===============================
    // ORDER NOT FOUND
    // ===============================
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleOrderNotFound(OrderNotFoundException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    // ===============================
    // INVALID STATE
    // ===============================
    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidState(InvalidOrderStateException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    // ===============================
    // EXTERNAL SERVICE FAILURE
    // ===============================
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ApiResponse<Object>> handleExternalService(ExternalServiceException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    // ===============================
    // VALIDATION ERROR
    // ===============================
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return ApiResponseBuilder.error(
                errorMessage,
                HttpStatus.BAD_REQUEST
        );
    }

    // ===============================
    // GENERIC ERROR (FALLBACK)
    // ===============================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {

        ex.printStackTrace(); // you can replace with logger

        return ApiResponseBuilder.error(
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}