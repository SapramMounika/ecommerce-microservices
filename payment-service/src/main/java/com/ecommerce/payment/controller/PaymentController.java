package com.ecommerce.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.response.ApiResponse;
import com.ecommerce.payment.service.PaymentService;


@RestController
@RequestMapping("/payments")
public class PaymentController {

	
	@Autowired
    private PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
    		@RequestHeader("Authorization") String token,
            @RequestBody PaymentRequest request) {
    	System.out.println("PAYMENT CONTROLLER HIT");
        return paymentService.processPayment(token,request);
    }




}
