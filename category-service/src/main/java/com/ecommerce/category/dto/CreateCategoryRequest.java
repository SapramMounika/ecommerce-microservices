package com.ecommerce.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCategoryRequest {

	@NotBlank(message = "Category name is required")
	 @Size(min = 3, max = 100, message = "Category name must be between 3 and 100 characters")
	private String name;
	
	@NotBlank(message = "Description is required")
	@Size(max = 500, message = "Description cannot exceed 500 characters")
	private String description;
	

    public CreateCategoryRequest() {
    }

    public CreateCategoryRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
    
    
	
}
