package com.ecommerce.userservice.service;

import java.util.List;

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
    
    
}
