package com.ecommerce.productservice.dto;



import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductCreateRequest {
	 @NotBlank(message = "Name is required")
	    private String name;

	    @NotBlank(message = "Description is required")
	    private String description;

	    @NotBlank(message = "Category is required")
	    private String category;

	    @NotNull(message = "Price is required")
	    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	    private Double price;

	    @NotNull(message = "Quantity is required")
	    @Min(value = 0, message = "Quantity cannot be negative")
	    private Integer quantity;
	    @NotBlank(message = "Stock status is required")
	    private String stock;

	    @NotNull(message = "Active status is required")
	    private Boolean active;
	    
	    //Getters and Setters
	    
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

		public void setPrice(@NotNull(message = "Price is required") @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") Double price) {
			this.price = price;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
	    
	    
}
