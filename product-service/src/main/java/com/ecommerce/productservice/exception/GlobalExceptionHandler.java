package com.ecommerce.productservice.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ecommerce.productservice.dto.ApiResponse;

@ControllerAdvice
	public class GlobalExceptionHandler {

	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<ApiResponse<Map<String,String>>> handleValidationException(
	            MethodArgumentNotValidException ex) {

	        Map<String,String> errors = new HashMap<>();

	        ex.getBindingResult().getFieldErrors().forEach(error -> {
	            errors.put(error.getField(), error.getDefaultMessage());
	        });

	        ApiResponse<Map<String,String>> response =
	                new ApiResponse<>(false, "Validation Failed", errors);

	        return ResponseEntity.badRequest().body(response);
	    }
	    
	    @ExceptionHandler(ProductNotFoundException.class)
	    public ResponseEntity<ApiResponse<Object>> handleProductNotFound(ProductNotFoundException ex) {

	        ApiResponse<Object> response =
	                new ApiResponse<>(false, ex.getMessage(), null);

	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }

}
