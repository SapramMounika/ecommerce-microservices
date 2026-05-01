package com.ecommerce.order.exception;

public class ExternalServiceException extends OrderServiceException {

	private static final long serialVersionUID = 1L;

	public ExternalServiceException(String serviceName) {
        super(serviceName + " service is unavailable", 503);
    }

}
