
	package com.ecommerce.userservice.service;

	import com.ecommerce.userservice.dto.*;

	public interface AuthService {

		RegisterResponse register(RegisterRequest request);

	    LoginResponse login(LoginRequest request);
	}

