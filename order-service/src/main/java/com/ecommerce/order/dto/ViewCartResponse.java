package com.ecommerce.order.dto;

import java.util.List;

public class ViewCartResponse {

	
	  private List<CartItemDto> items;

	  public List<CartItemDto> getItems() {
		  return items;
	  }

	  public void setItems(List<CartItemDto> items) {
		  this.items = items;
	  }
	  
	  
}
