package com.ecommerce.cart.exception;

public class CartItemAlreadyExistsException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public CartItemAlreadyExistsException(String message) {
        super(message);
    }

}
