package com.ecommerce.userservice.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecommerce.userservice.dto.PageResponse;
import com.ecommerce.userservice.dto.UserCreateRequest;
import com.ecommerce.userservice.dto.UserCreateResponse;
import com.ecommerce.userservice.dto.UserDeleteResponse;
import com.ecommerce.userservice.dto.UserGetResponse;
import com.ecommerce.userservice.dto.UserUpdateRequest;
import com.ecommerce.userservice.dto.UserUpdateResponse;

public interface UserService {

	 // VIEW ALL
    List<UserGetResponse> getAllUsers();

    // VIEW BY ID
    UserGetResponse getUserById(Long id);

    // CREATE (ADMIN)
    UserCreateResponse createUser(UserCreateRequest request);

    // UPDATE (ADMIN)
    UserUpdateResponse updateUser(Long id, UserUpdateRequest request);

    // DELETE (ADMIN)
    UserDeleteResponse deleteUser(Long id);
    
    //GET users with pagination
    Page<UserGetResponse> getUsersWithPagination(
            int page,
            int size,
            String sortBy,
            String direction
    );
    
    /**
     * Searches users with support for:
     * - Pagination
     * - Sorting
     * - Filtering (by username and role)
     *
     * @param page       page number (0-based index)
     * @param size       number of records per page
     * @param sortBy     field name to sort by (id, username, role)
     * @param direction  sorting direction (asc or desc)
     * @param username   optional username filter (contains search)
     * @param role       optional role filter
     *
     * @return PageResponse containing user data and pagination metadata
     */
    PageResponse<UserGetResponse> searchUsers(
            int page,
            int size,
            String sortBy,
            String direction,
            String username,
            String role
    );
    
}
