package com.ecommerce.order.exception;

public class InvalidOrderStateException extends OrderServiceException {

	private static final long serialVersionUID = 1L;

	public InvalidOrderStateException(String message) {
        super(message, 400);
    }

}
