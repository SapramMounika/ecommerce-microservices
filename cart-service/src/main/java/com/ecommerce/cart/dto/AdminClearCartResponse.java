package com.ecommerce.cart.dto;

import java.util.List;

public class AdminClearCartResponse {

	
	private Long cartId;
    private Long userId;
    private List<Item> items;
    private Double totalAmount;

    public static class Item {
        private Long cartItemId;
        private Long productId;
        private Integer quantity;
        private Double price;
        private Double totalPrice;
		public Long getCartItemId() {
			return cartItemId;
		}
		public void setCartItemId(Long cartItemId) {
			this.cartItemId = cartItemId;
		}
		public Long getProductId() {
			return productId;
		}
		public void setProductId(Long productId) {
			this.productId = productId;
		}
		public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
		public Double getPrice() {
			return price;
		}
		public void setPrice(Double price) {
			this.price = price;
		}
		public Double getTotalPrice() {
			return totalPrice;
		}
		public void setTotalPrice(Double totalPrice) {
			this.totalPrice = totalPrice;
		}
           
}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
    
    
    
}
