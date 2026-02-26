package com.ecommerce.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.ecommerce.userservice.dto.LoginRequest;
import com.ecommerce.userservice.dto.LoginResponse;
import com.ecommerce.userservice.dto.RegisterRequest;
import com.ecommerce.userservice.dto.RegisterResponse;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.exception.UserAlreadyExistsException;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {
@Autowired
    private final UserRepository userRepository;

  @Autowired
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                          
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
      
        this.jwtUtil = jwtUtil;
    }

    
    // REGISTER
    
    @Override
    public RegisterResponse register(RegisterRequest request) {

        //  Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
        	throw new UserAlreadyExistsException("Username already exists");
        }

        //  Create new user
        User user = new User();
        user.setUsername(request.getUsername());

        // Encrypt password
        user.setPassword(request.getPassword());

        // IMPORTANT: Always ROLE_USER
        
        user.setRole("ROLE_USER");

        //  Save to DB
        User savedUser = userRepository.save(user);

        //  Return response
        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole(),
                "User registered successfully"
        );
    }

   
    // LOGIN
   
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if (!request.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole()
        );
        System.out.println("Generated Token: " + token);
        return new LoginResponse(token);
    }
}