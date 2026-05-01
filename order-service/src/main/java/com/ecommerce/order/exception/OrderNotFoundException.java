
// Order Not Found Exception

package com.ecommerce.order.exception;

public class OrderNotFoundException extends OrderServiceException {

	private static final long serialVersionUID = 1L;

	public OrderNotFoundException(Long orderId) {
        super("Order not found: " + orderId, 404);
    }

}
