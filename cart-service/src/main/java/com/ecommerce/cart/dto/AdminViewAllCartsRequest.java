package com.ecommerce.cart.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class AdminViewAllCartsRequest {

	
	 @Min(value = 0, message = "Page must be zero or greater")
	    private int page = 0;

	    @Min(value = 1, message = "Size must be at least 1")
	    @Max(value = 100, message = "Size cannot exceed 100")
	    private int size = 10;

		public int getPage() {
			return page;
		}

		public void setPage(int page) {
			this.page = page;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}
	    
	    
	    
}
