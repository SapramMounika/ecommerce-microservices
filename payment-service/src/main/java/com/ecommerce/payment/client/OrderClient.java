package com.ecommerce.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ecommerce.payment.dto.UpdateOrderStatusRequest;
import com.ecommerce.payment.dto.UpdateOrderStatusResponse;
import com.ecommerce.payment.response.ApiResponse;

@FeignClient(name = "order-service")
public interface OrderClient {


    @PutMapping("/api/orders/internal/status")
    ResponseEntity<ApiResponse<UpdateOrderStatusResponse>> updateOrderStatus(
    		@RequestHeader("Authorization") String token,
            @RequestBody UpdateOrderStatusRequest request
    );
	
}
