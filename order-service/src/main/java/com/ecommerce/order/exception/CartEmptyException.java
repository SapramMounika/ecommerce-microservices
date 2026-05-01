
// Cart Empty Exception


package com.ecommerce.order.exception;

public class CartEmptyException extends OrderServiceException {

	private static final long serialVersionUID = 1L;

	public CartEmptyException() {
        super("Cart is empty", 400);
    }

	
	
}
