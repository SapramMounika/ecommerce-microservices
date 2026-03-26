package com.ecommerce.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.cart.dto.ProductResponse;
import com.ecommerce.cart.response.ApiResponse; // ✅ use existing

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("/api/products/viewById/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable("id") Long id);
}