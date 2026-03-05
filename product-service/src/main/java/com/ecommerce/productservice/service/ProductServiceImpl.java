package com.ecommerce.productservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.productservice.dto.ApiResponse;
import com.ecommerce.productservice.dto.*;
import com.ecommerce.productservice.entity.Inventory;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import com.ecommerce.productservice.repository.ProductRepository;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ================= CREATE =================
    @Override
    public ApiResponse<ProductCreateResponse> createProduct(ProductCreateRequest request) {

        boolean exists = productRepository
                .existsByNameAndCategory(request.getName(), request.getCategory());

        if (exists) {
            return new ApiResponse<>(false, "Product already exists", null);
        }

        // Create Product
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setActive(request.getActive());

        // Create Inventory
        Inventory inventory = new Inventory();
        inventory.setQuantity(request.getQuantity());
        inventory.setStock(request.getQuantity() > 0 ? "IN_STOCK" : "OUT_OF_STOCK");
        inventory.setActive(true);

        inventory.setProduct(product);
        product.setInventory(inventory);

        Product savedProduct = productRepository.save(product);

        // Prepare Response
        ProductCreateResponse response = new ProductCreateResponse();
        response.setProductId(savedProduct.getId());
        response.setName(savedProduct.getName());
        response.setDescription(savedProduct.getDescription());
        response.setCategory(savedProduct.getCategory());
        response.setPrice(savedProduct.getPrice());
        response.setQuantity(savedProduct.getInventory().getQuantity());
        response.setStock(savedProduct.getInventory().getStock());
        response.setActive(savedProduct.getActive());
       

        return new ApiResponse<>(true, "Product created successfully", response);
    }

    // ================= GET ALL =================
    @Override
    public ApiResponse<List<ProductGetResponse>> getAllProducts() {

        List<Product> products = productRepository.findAll();
        List<ProductGetResponse> responseList = new ArrayList<>();

        for (Product product : products) {

            ProductGetResponse response = new ProductGetResponse();
            response.setProductId(product.getId());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setCategory(product.getCategory());
            response.setPrice(product.getPrice());
            response.setQuantity(product.getInventory().getQuantity());
            response.setStock(product.getInventory().getStock());
            response.setActive(product.getActive());

            responseList.add(response);
        }

        return new ApiResponse<>(true, "Products fetched successfully", responseList);
    }

    // ================= GET BY ID =================
    @Override
    public ApiResponse<ProductGetResponse> getProductById(Long id) {

        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            return new ApiResponse<>(false, "Product not found", null);
        }

        Product product = optionalProduct.get();

        ProductGetResponse response = new ProductGetResponse();
        response.setProductId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getInventory().getQuantity());
        response.setStock(product.getInventory().getStock());
        response.setActive(product.getActive());

        return new ApiResponse<>(true, "Product fetched successfully", response);
    }

    // ================= UPDATE =================
    @Override
    public ApiResponse<ProductUpdateResponse> updateProduct(Long id, ProductUpdateRequest request) {

        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            return new ApiResponse<>(false, "Product not found", null);
        }

        Product product = optionalProduct.get();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setActive(request.getActive());

        Inventory inventory = product.getInventory();
        inventory.setQuantity(request.getQuantity());
        inventory.setStock(request.getQuantity() > 0 ? "IN_STOCK" : "OUT_OF_STOCK");

        Product updatedProduct = productRepository.save(product);

        ProductUpdateResponse response = new ProductUpdateResponse();
        response.setProductId(updatedProduct.getId());
        response.setName(updatedProduct.getName());
        response.setDescription(updatedProduct.getDescription());
        response.setCategory(updatedProduct.getCategory());
        response.setPrice(updatedProduct.getPrice());
        response.setQuantity(updatedProduct.getInventory().getQuantity());
        response.setStock(updatedProduct.getInventory().getStock());
        response.setActive(updatedProduct.getActive());
        

        return new ApiResponse<>(true, "Product updated successfully", response);
    }

    // ================= DELETE (SOFT DELETE) =================
    @Override
    public ApiResponse<ProductDeleteResponse> DeleteProduct(Long id) {

        Product product = productRepository.findById(id)
        		.orElseThrow(() -> new ProductNotFoundException("Product not found"));

        productRepository.delete(product);

        ProductDeleteResponse response = new ProductDeleteResponse();
        response.setProductId(id);

        return new ApiResponse<>(true, "Product deleted successfully", response);
    }
}