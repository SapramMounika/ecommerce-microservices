package com.ecommerce.userservice.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.userservice.dto.*;
import com.ecommerce.userservice.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    
    // VIEW ALL (ADMIN + USER)
    
    @GetMapping("/viewAll")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<List<UserGetResponse>> getAllUsers() {

        List<UserGetResponse> users = userService.getAllUsers();

        return new ApiResponse<>(
                "SUCCESS",
                "Users fetched successfully",
                users
        );
    }
    
   
    // VIEW BY ID (ADMIN + USER)
   
    @GetMapping("viewById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<UserGetResponse> getUserById(@PathVariable Long id) {

        UserGetResponse user = userService.getUserById(id);

        return new ApiResponse<>(
                "SUCCESS",
                "User fetched successfully",
                user
        );
    }

    
    // CREATE USER (ADMIN ONLY)
    
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserCreateResponse> createUser(
    		@Valid @RequestBody UserCreateRequest request) {

        UserCreateResponse response = userService.createUser(request);

        return new ApiResponse<>(
                "SUCCESS",
                "User created successfully",
                response
        );
    }
   
    // UPDATE USER (ADMIN ONLY)
   
    @PutMapping("update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserUpdateResponse> updateUser(
            @PathVariable Long id,
            @Valid  @RequestBody UserUpdateRequest request) {

        UserUpdateResponse response =
                userService.updateUser(id, request);

        return new ApiResponse<>(
                "SUCCESS",
                "User updated successfully",
                response
        );
    }

    
    // DELETE USER (ADMIN ONLY)
   
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserDeleteResponse> deleteUser(
            @PathVariable Long id) {

        UserDeleteResponse response =
                userService.deleteUser(id);

        return new ApiResponse<>(
                "SUCCESS",
                "User deleted successfully",
                response
        );
    }
    
    //Get User With Pagination
    
    @GetMapping("/paginated")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Page<UserGetResponse> getUsersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return userService.getUsersWithPagination(page, size, sortBy, direction);
    }
    /**
     * Advanced search endpoint supporting:
     * - Pagination
     * - Sorting
     * - Filtering (username, role)
     *
     * Example:
     * GET /api/users/search?page=0&size=5&sortBy=username&direction=asc&username=la&role=ROLE_USER
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public PageResponse<UserGetResponse> searchUsers(

            // Page number (default 0)
            @RequestParam(defaultValue = "0") int page,

            // Number of records per page (default 5)
            @RequestParam(defaultValue = "5") int size,

            // Field to sort by (default id)
            @RequestParam(defaultValue = "id") String sortBy,

            // Sorting direction (asc or desc)
            @RequestParam(defaultValue = "asc") String direction,

            // Optional filter: username contains search
            @RequestParam(required = false) String username,

            // Optional filter: role contains search
            @RequestParam(required = false) String role
    ) {

        return userService.searchUsers(
                page,
                size,
                sortBy,
                direction,
                username,
                role
        );
    }
}