package com.ecommerce.userservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserCreateRequest {
	@Column
	@NotBlank(message = "Username is required")
	private String username;
	@Column
	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;
	@NotBlank(message = "Role is required")
	@Pattern(regexp = "ROLE_ADMIN|ROLE_USER", message = "Role must be ROLE_ADMIN or ROLE_USER")
	private String role; // ROLE_USER or ROLE_ADMIN

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
