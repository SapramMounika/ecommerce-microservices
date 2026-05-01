package com.ecommerce.order.enums;

public enum OrderStatus {

	

    CREATED,        // Order created but not processed
    PENDING,        // Waiting for payment / confirmation
    CONFIRMED,      // Order confirmed
    SHIPPED,        // Order shipped
    DELIVERED,      // Delivered to customer
    CANCELLED,      // Cancelled by user/admin
    FAILED          // Failed due to error (stock/payment)
}
