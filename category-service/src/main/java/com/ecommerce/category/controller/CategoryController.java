package com.ecommerce.category.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.category.dto.CreateCategoryRequest;
import com.ecommerce.category.dto.CreateCategoryResponse;
import com.ecommerce.category.dto.DeleteCategoryResponse;
import com.ecommerce.category.dto.UpdateCategoryRequest;
import com.ecommerce.category.dto.UpdateCategoryResponse;
import com.ecommerce.category.dto.ViewCategoryResponse;
import com.ecommerce.category.response.ApiResponse;
import com.ecommerce.category.response.ApiResponseBuilder;
import com.ecommerce.category.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    // CREATE CATEGORY (ADMIN ONLY)
    
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CreateCategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {

        CreateCategoryResponse response = categoryService.createCategory(request);

        return ApiResponseBuilder.success(
                "Category created successfully",
                response,
                HttpStatus.CREATED
        );
    }

    
    // UPDATE CATEGORY (ADMIN ONLY)
    
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<UpdateCategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {

        UpdateCategoryResponse response = categoryService.updateCategory(id, request);

        return ApiResponseBuilder.success(
                "Category updated successfully",
                response,
                HttpStatus.OK
        );
    }

    
    // DELETE CATEGORY (ADMIN ONLY)
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<DeleteCategoryResponse>> deleteCategory(
            @PathVariable Long id) {

        DeleteCategoryResponse response = categoryService.deleteCategory(id);

        return ApiResponseBuilder.success(
                "Category deleted successfully",
                response,
                HttpStatus.OK
        );
    }

    
    // VIEW ALL CATEGORIES (USER + ADMIN)
    
    @GetMapping("/viewAll")
    public ResponseEntity<ApiResponse<List<ViewCategoryResponse>>> getAllCategories() {

        List<ViewCategoryResponse> response = categoryService.getAllCategories();

        return ApiResponseBuilder.success(
                "Categories fetched successfully",
                response,
                HttpStatus.OK
        );
    }

    
    // VIEW CATEGORY BY ID (USER + ADMIN)
 
    @GetMapping("/viewById/{id}")
    public ResponseEntity<ApiResponse<ViewCategoryResponse>> getCategoryById(
            @PathVariable Long id) {

        ViewCategoryResponse response = categoryService.getCategoryById(id);

        return ApiResponseBuilder.success(
                "Category fetched successfully",
                response,
                HttpStatus.OK
        );
    }
}