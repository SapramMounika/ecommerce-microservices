package com.ecommerce.payment.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.payment.client.OrderClient;
import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.dto.UpdateOrderStatusRequest;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.enums.PaymentStatus;
import com.ecommerce.payment.exception.PaymentFailedException;
import com.ecommerce.payment.repository.PaymentRepository;
import com.ecommerce.payment.response.ApiResponse;
import com.ecommerce.payment.response.ApiResponseBuilder;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderClient orderClient;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderClient orderClient) {

        this.paymentRepository = paymentRepository;
        this.orderClient = orderClient;
    }

    @Override
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            String token,
            PaymentRequest request) {

        System.out.println("==================================");
        System.out.println("PAYMENT SERVICE HIT");
        System.out.println("ORDER ID : " + request.getOrderId());
        System.out.println("AMOUNT : " + request.getAmount());
        System.out.println("PAYMENT METHOD : " + request.getPaymentMethod());
        System.out.println("==================================");

        // 🔥 Simulate payment success
        boolean paymentSuccess = true;

        // 🔥 CREATE PAYMENT ENTITY
        Payment payment = new Payment();

        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentTime(LocalDateTime.now());

        // ==========================================
        // ✅ PAYMENT SUCCESS
        // ==========================================
        if (paymentSuccess) {

            System.out.println("PAYMENT SUCCESS");

            payment.setPaymentStatus(PaymentStatus.SUCCESS);

            // 🔥 SAVE PAYMENT
            paymentRepository.save(payment);

            System.out.println("PAYMENT SAVED IN DATABASE");

            // 🔥 CREATE ORDER STATUS UPDATE REQUEST
            UpdateOrderStatusRequest updateRequest =
                    new UpdateOrderStatusRequest();

            updateRequest.setOrderId(request.getOrderId());
            updateRequest.setStatus("CONFIRMED");

            try {

                // 🔥 CALL ORDER SERVICE
                System.out.println("CALLING ORDER SERVICE...");

                orderClient.updateOrderStatus(token, updateRequest);

                System.out.println("ORDER SERVICE CALLED SUCCESSFULLY");

            } catch (Exception e) {

                System.out.println("ERROR WHILE CALLING ORDER SERVICE");

                e.printStackTrace();

                throw e;
            }

            // 🔥 CREATE RESPONSE
            PaymentResponse response = new PaymentResponse();

            response.setOrderId(request.getOrderId());
            response.setPaymentStatus(PaymentStatus.SUCCESS.name());
            response.setMessage("Payment completed successfully");

            System.out.println("PAYMENT FLOW COMPLETED");

            return ApiResponseBuilder.success(
                    "Payment success",
                    response,
                    HttpStatus.OK
            );

        }

        // ==========================================
        // ❌ PAYMENT FAILED
        // ==========================================
        else {

            System.out.println("PAYMENT FAILED");

            payment.setPaymentStatus(PaymentStatus.FAILED);

            paymentRepository.save(payment);

            throw new PaymentFailedException("Payment failed");
        }
    }
}