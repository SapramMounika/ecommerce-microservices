package com.ecommerce.productservice.service;

import java.util.List;

import com.ecommerce.productservice.dto.ApiResponse;
import com.ecommerce.productservice.dto.ProductCreateRequest;
import com.ecommerce.productservice.dto.ProductCreateResponse;
import com.ecommerce.productservice.dto.ProductDeleteResponse;
import com.ecommerce.productservice.dto.ProductGetResponse;
import com.ecommerce.productservice.dto.ProductUpdateRequest;
import com.ecommerce.productservice.dto.ProductUpdateResponse;

public interface ProductService {
	
	ApiResponse<ProductCreateResponse> createProduct(ProductCreateRequest request);

    ApiResponse<ProductUpdateResponse> updateProduct(Long id, ProductUpdateRequest request);

    ApiResponse<ProductGetResponse> getProductById(Long id);

    ApiResponse<List<ProductGetResponse>> getAllProducts();
    
    ApiResponse<ProductDeleteResponse> DeleteProduct(Long id);

}
