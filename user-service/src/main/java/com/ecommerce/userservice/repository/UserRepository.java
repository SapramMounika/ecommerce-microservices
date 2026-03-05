package com.ecommerce.userservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.userservice.entity.User;

@Repository
	public interface UserRepository extends JpaRepository<User, Long> {

	    Optional<User> findByUsername(String username);
	    //This allows filtering by username and role.
	    Page<User> findByUsernameContainingIgnoreCaseAndRoleContainingIgnoreCase(
	            String username,
	            String role,
	            Pageable pageable
	    );
	}


