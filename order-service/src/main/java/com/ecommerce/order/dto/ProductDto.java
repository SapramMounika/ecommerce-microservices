package com.ecommerce.order.dto;

public class ProductDto {
	
	
	
	private Long productId;
    private Double price;
    private String stock;
    
    
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
    
    
    
}
