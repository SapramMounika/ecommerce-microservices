package com.ecommerce.order.dto;

public class UpdateOrderStatusResponse {
//Admin
	
	 private Long orderId;
	    private String status;
	    private String message;
		public Long getOrderId() {
			return orderId;
		}
		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	    
	    
	
}
