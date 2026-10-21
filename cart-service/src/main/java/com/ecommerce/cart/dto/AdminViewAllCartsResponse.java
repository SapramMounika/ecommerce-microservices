package com.ecommerce.cart.dto;

import java.util.List;

public class AdminViewAllCartsResponse {

	private List<CartDetails> carts;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    

    public static class CartDetails {
        private Long cartId;
        private Long userId;
        private List<AddToCartResponse.Item> items;
        private Integer totalItems;
        private Double totalAmount;
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
		public List<AddToCartResponse.Item> getItems() {
			return items;
		}
		public void setItems(List<AddToCartResponse.Item> items) {
			this.items = items;
		}
		public Integer getTotalItems() {
			return totalItems;
		}
		public void setTotalItems(Integer totalItems) {
			this.totalItems = totalItems;
		}
		public Double getTotalAmount() {
			return totalAmount;
		}
		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}
	
}

	public List<CartDetails> getCarts() {
		return carts;
	}

	public void setCarts(List<CartDetails> carts) {
		this.carts = carts;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}


    
    
}