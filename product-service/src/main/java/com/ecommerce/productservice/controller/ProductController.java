package com.ecommerce.productservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.ecommerce.productservice.dto.ApiResponse;
import com.ecommerce.productservice.dto.ProductCreateRequest;
import com.ecommerce.productservice.dto.ProductCreateResponse;
import com.ecommerce.productservice.dto.ProductDeleteResponse;
import com.ecommerce.productservice.dto.ProductGetResponse;

import com.ecommerce.productservice.dto.ProductUpdateRequest;
import com.ecommerce.productservice.dto.ProductUpdateResponse;
import com.ecommerce.productservice.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ✅ Create Product
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<ProductCreateResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {

        ApiResponse<ProductCreateResponse> response =
                productService.createProduct(request);

        return ResponseEntity.ok(response);
    }

    // ✅ Get All Products
    @GetMapping("viewAll")
    public ResponseEntity<ApiResponse<List<ProductGetResponse>>> getAllProducts() {

        ApiResponse<List<ProductGetResponse>> response =
                productService.getAllProducts();

        return ResponseEntity.ok(response);
    }

    // ✅ Get Product By ID
    @GetMapping("viewById/{id}")
    public ResponseEntity<ApiResponse<ProductGetResponse>> getProductById(
            @PathVariable Long id) {

        ApiResponse<ProductGetResponse> response =
                productService.getProductById(id);

        return ResponseEntity.ok(response);
    }

    // ✅ Update Product
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<ProductUpdateResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request) {

        ApiResponse<ProductUpdateResponse> response =
                productService.updateProduct(id, request);

        return ResponseEntity.ok(response);
    }

    // ✅ Delete Product
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<ProductDeleteResponse>> deleteProduct(
            @PathVariable Long id) {

        ApiResponse<ProductDeleteResponse> response =
                productService.DeleteProduct(id);

        return ResponseEntity.ok(response);
    }
}