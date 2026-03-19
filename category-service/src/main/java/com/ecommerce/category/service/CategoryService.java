package com.ecommerce.category.service;

import java.util.List;

import com.ecommerce.category.dto.CreateCategoryRequest;
import com.ecommerce.category.dto.CreateCategoryResponse;
import com.ecommerce.category.dto.UpdateCategoryRequest;
import com.ecommerce.category.dto.UpdateCategoryResponse;
import com.ecommerce.category.dto.DeleteCategoryResponse;
import com.ecommerce.category.dto.ViewCategoryResponse;

public interface CategoryService {

    CreateCategoryResponse createCategory(CreateCategoryRequest request);

    UpdateCategoryResponse updateCategory(Long id, UpdateCategoryRequest request);

    DeleteCategoryResponse deleteCategory(Long id);

    List<ViewCategoryResponse> getAllCategories();

    ViewCategoryResponse getCategoryById(Long id);

}