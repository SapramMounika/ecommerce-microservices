package com.ecommerce.userservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecommerce.userservice.dto.*;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.exception.DuplicateResourceException;
import com.ecommerce.userservice.exception.ResourceNotFoundException;
import com.ecommerce.userservice.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    // VIEW ALL
   
    @Override
    public List<UserGetResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserGetResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()))
                .collect(Collectors.toList());
    }

    
    // VIEW BY ID
   
    @Override
    public UserGetResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return new UserGetResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

    
    // CREATE USER (ADMIN)
    
    /*Flow should be:

Check if exists

If exists → throw exception

Else → create user

Save user

Return response*/
    @Override
    public UserCreateResponse createUser(UserCreateRequest request) {

        // 1️⃣ Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists");
        }

        // 2️⃣ Create new user object
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // plain text (as you decided)
        user.setRole(request.getRole());

        // 3️⃣ Save user
        User saved = userRepository.save(user);

        // 4️⃣ Return response
        return new UserCreateResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getRole(),
                "User created successfully"
        );
    }

    
    // UPDATE USER (ADMIN)
    
    @Override
    public UserUpdateResponse updateUser(Long id, UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getUsername());
        user.setRole(request.getRole());

        User updated = userRepository.save(user);

        return new UserUpdateResponse(
                updated.getId(),
                updated.getUsername(),
                updated.getRole(),
                "User updated successfully"
        );
    }

    
    // DELETE USER (ADMIN)
   
    @Override
    public UserDeleteResponse deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);

        return new UserDeleteResponse("User deleted successfully");
    }
}