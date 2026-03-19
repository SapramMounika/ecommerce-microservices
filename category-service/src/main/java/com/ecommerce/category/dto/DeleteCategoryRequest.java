package com.ecommerce.category.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DeleteCategoryRequest {

    @NotNull(message = "Category id is required")
    @Positive(message = "Category id must be positive")
    private Long id;

    public DeleteCategoryRequest() {
    }

    public DeleteCategoryRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
