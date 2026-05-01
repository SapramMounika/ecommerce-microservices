package com.ecommerce.order.exception;

public class OrderServiceException extends RuntimeException{


	private static final long serialVersionUID = 1L;
	private final int status;

    public OrderServiceException(String message, int status) {
        super(message);
        this.status = status;
    }

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getStatus() {
		return status;
	}
    
    
	
}
