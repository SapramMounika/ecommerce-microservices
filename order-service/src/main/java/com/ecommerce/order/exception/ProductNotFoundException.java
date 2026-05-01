
//Product Not Found Exception

package com.ecommerce.order.exception;

public class ProductNotFoundException extends OrderServiceException {

	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(Long productId) {
        super("Product not found: " + productId, 404);
    }
}


