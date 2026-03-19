package com.ecommerce.category.dto;

public class CreateCategoryResponse {

    private Long id;
    private String name;
    private String description;

    // No-args constructor
    public CreateCategoryResponse() {
    }

    // All-args constructor
    public CreateCategoryResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getter for id
    public Long getId() {
        return id;
    }

    // Setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for description
    public String getDescription() {
        return description;
    }

    // Setter for description
    public void setDescription(String description) {
        this.description = description;
    }
}
	
	
	
	
	
	
	
	
	

