package com.ecommerce.productservice.dto;

public class ProductUpdateResponse {

    private Long productId;

    private String name;
    private String description;
    private String category;
    private Double price;

    private Integer quantity;
    private String stock;

    private Boolean active;

    
//default constructor
    public ProductUpdateResponse() {
    }
//parameterized constructor
    public ProductUpdateResponse(Long productId,
                                 String name,
                                 String description,
                                 String category,
                                 Double price,
                                 Integer quantity,
                                 String stock,
                                 Boolean active,
                                 String message) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.stock = stock;
        this.active = active;
        
    }

    // Getters & Setters

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

   
}
