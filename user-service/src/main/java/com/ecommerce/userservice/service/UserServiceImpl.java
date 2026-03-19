package com.ecommerce.userservice.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
                saved.getRole()
               
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
    //Get User With Pagination
    @Override
    public Page<UserGetResponse> getUsersWithPagination(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.map(user ->
                new UserGetResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                )
        );
    }
    
    @Override
    public PageResponse<UserGetResponse> searchUsers(
            int page,
            int size,
            String sortBy,
            String direction,
            String username,
            String role) {

        // ---------------------------------------------------------
        // 1️⃣ Protect against extremely large page sizes
        //    (Prevents performance issues and abuse attacks)
        // ---------------------------------------------------------
        int maxSize = 50;
        if (size > maxSize) {
            size = maxSize;
        }

        // ---------------------------------------------------------
        // 2️⃣ Validate allowed sort fields
        //    Prevents sorting by unsafe or invalid fields
        // ---------------------------------------------------------
        List<String> allowedSortFields = List.of("id", "username", "role");

        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "id"; // fallback to safe default
        }

        // ---------------------------------------------------------
        // 3️⃣ Create Sort object dynamically
        // ---------------------------------------------------------
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // ---------------------------------------------------------
        // 4️⃣ Create Pageable object
        //    Converts page + size + sort into SQL LIMIT & OFFSET
        // ---------------------------------------------------------
        Pageable pageable = PageRequest.of(page, size, sort);

        // ---------------------------------------------------------
        // 5️⃣ Null-safe filtering
        //    If filter parameters are null, use empty string
        //    This allows "contains" query to return all records
        // ---------------------------------------------------------
        String usernameFilter = (username == null) ? "" : username;
        String roleFilter = (role == null) ? "" : role;

        // ---------------------------------------------------------
        // 6️⃣ Execute repository query with filtering + pagination
        //    Spring automatically generates:
        //    - SELECT query with LIMIT and OFFSET
        //    - COUNT query for totalElements
        // ---------------------------------------------------------
        Page<User> userPage =
                userRepository.findByUsernameContainingIgnoreCaseAndRoleContainingIgnoreCase(
                        usernameFilter,
                        roleFilter,
                        pageable
                );

        // ---------------------------------------------------------
        // 7️⃣ Convert Entity -> DTO
        //    Prevents exposing internal entity structure
        // ---------------------------------------------------------
        List<UserGetResponse> users = userPage.getContent().stream()
                .map(user -> new UserGetResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                ))
                .toList();

        // ---------------------------------------------------------
        // 8️⃣ Build pagination metadata
        // ---------------------------------------------------------
        PaginationResponse pagination = new PaginationResponse(
                userPage.getNumber(),          // current page number
                userPage.getSize(),            // page size
                userPage.getTotalElements(),   // total records in DB
                userPage.getTotalPages(),      // total pages available
                userPage.isFirst(),            // is first page?
                userPage.isLast()              // is last page?
        );

        // ---------------------------------------------------------
        // 9️⃣ Wrap data + pagination into clean response object
        // ---------------------------------------------------------
        return new PageResponse<>(users, pagination);
    }
}