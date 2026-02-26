package com.ecommerce.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserUpdateRequest {
	@NotBlank(message = "Username is required")
	 private String username;
	
	 @NotBlank(message = "Role is required")
	    @Pattern(regexp = "ROLE_ADMIN|ROLE_USER",
	             message = "Role must be ROLE_ADMIN or ROLE_USER")
	    private String role;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
}
