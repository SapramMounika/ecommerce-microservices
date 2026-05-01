
// Product Out Of Stock Exception

package com.ecommerce.order.exception;

public class ProductOutOfStockException extends OrderServiceException {

	private static final long serialVersionUID = 1L;

	public ProductOutOfStockException(Long productId) {
        super("Product out of stock: " + productId, 400);
    }

}
