package com.ecommerce.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.payment.response.ApiResponse;
import com.ecommerce.payment.response.ApiResponseBuilder;

@RestControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ApiResponse<Object>> handlePaymentFailedException(
            PaymentFailedException ex) {

        return ApiResponseBuilder.error(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
}
