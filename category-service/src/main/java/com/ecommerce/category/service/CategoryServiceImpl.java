package com.ecommerce.category.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecommerce.category.dto.CreateCategoryRequest;
import com.ecommerce.category.dto.CreateCategoryResponse;
import com.ecommerce.category.dto.DeleteCategoryResponse;
import com.ecommerce.category.dto.UpdateCategoryRequest;
import com.ecommerce.category.dto.UpdateCategoryResponse;
import com.ecommerce.category.dto.ViewCategoryResponse;
import com.ecommerce.category.entity.Category;
import com.ecommerce.category.exception.CategoryAlreadyExistsException;
import com.ecommerce.category.exception.CategoryNotFoundException;
import com.ecommerce.category.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    
    // CREATE CATEGORY (ADMIN)
    
    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest request) {

        // 🔴 Check if category already exists
        if (categoryRepository.existsByName(request.getName())) {
            throw new CategoryAlreadyExistsException(
                    "Category already exists"
            );
        }

        // ✅ Create new category
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category saved = categoryRepository.save(category);

        // ✅ Return response DTO
        return new CreateCategoryResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription()
        );
    }

    
    // UPDATE CATEGORY (ADMIN)
    
    @Override
    public UpdateCategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {

        // 🔴 Check if category exists
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found "
                ));

        // ✅ Update values
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updated = categoryRepository.save(category);

        return new UpdateCategoryResponse(
                updated.getId(),
                updated.getName(),
                updated.getDescription()
        );
    }

    
    // DELETE CATEGORY (ADMIN)
   
    @Override
    public DeleteCategoryResponse deleteCategory(Long id) {

        // 🔴 Check if exists
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found "
                ));

        categoryRepository.delete(category);

        return new DeleteCategoryResponse(
                id
        );
    }

    
    // VIEW ALL CATEGORIES (USER + ADMIN)
   
    @Override
    public List<ViewCategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(cat -> new ViewCategoryResponse(
                        cat.getId(),
                        cat.getName(),
                        cat.getDescription()
                ))
                .collect(Collectors.toList());
    }

    
    // VIEW CATEGORY BY ID (USER + ADMIN)
    
    @Override
    public ViewCategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found"
                ));

        return new ViewCategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}