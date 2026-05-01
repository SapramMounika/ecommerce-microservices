package com.ecommerce.userservice.dto;

public class UserDeleteResponse {
	private Long userId;

    public UserDeleteResponse(Long userId) {
        this.userId = userId;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

   
}
