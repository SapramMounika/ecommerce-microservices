package com.ecommerce.userservice.service;

import org.springframework.stereotype.Service;

import com.ecommerce.userservice.dto.LoginRequest;
import com.ecommerce.userservice.dto.LoginResponse;
import com.ecommerce.userservice.dto.RegisterRequest;
import com.ecommerce.userservice.dto.RegisterResponse;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.exception.InvalidCredentialsException;
import com.ecommerce.userservice.exception.UserAlreadyExistsException;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // REGISTER
    @Override
    public RegisterResponse register(RegisterRequest request) {

        // ✅ 1. Validate request object
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        // ✅ 2. Validate username
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        // ✅ 3. Validate password
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // ✅ 4. Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        // ✅ 5. Create user
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(request.getPassword().trim()); // ⚠️ hash in real apps
        user.setRole("ROLE_USER");

        // ✅ 6. Save user
        User savedUser = userRepository.save(user);

        // ✅ 7. Return response
        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }

    // LOGIN 
    @Override
    public LoginResponse login(LoginRequest request) {

        // ✅ 1. Validate request
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // ✅ 2. Fetch user
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        // ✅ 3. Validate password
        if (!request.getPassword().equals(user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // ✅ 4. Generate JWT
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole()
        );

        // ✅ 5. Return response
        return new LoginResponse(token);
    }
}