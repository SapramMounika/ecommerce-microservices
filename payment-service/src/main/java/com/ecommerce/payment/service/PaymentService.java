package com.ecommerce.payment.service;

import org.springframework.http.ResponseEntity;

import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.response.ApiResponse;

public interface PaymentService {

	ResponseEntity<ApiResponse<PaymentResponse>> processPayment(String token,PaymentRequest request);
}
