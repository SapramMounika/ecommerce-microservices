package com.ecommerce.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AdminViewCartByCartIdRequest {

	
	
	 @NotNull(message = "Cart ID is required")
	    @Positive(message = "Cart ID must be positive")
	    private Long cartId;

	 public Long getCartId() {
		 return cartId;
	 }

	 public void setCartId(Long cartId) {
		 this.cartId = cartId;
	 }
	 
	 
}
