package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.LoginRequest;
import com.ecommerce.userservice.dto.LoginResponse;
import com.ecommerce.userservice.dto.RegisterRequest;
import com.ecommerce.userservice.dto.RegisterResponse;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.exception.InvalidCredentialsException;
import com.ecommerce.userservice.exception.UserAlreadyExistsException;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.util.JwtUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    // ✅ 1. SUCCESS REGISTER
    @Test
    void shouldRegisterUserSuccessfully() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("username");
        request.setPassword("password");

        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.empty());

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("username");
        savedUser.setPassword("password");
        savedUser.setRole("ROLE_USER");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        RegisterResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("username", response.getUsername());
    }

    // ❌ 2. DUPLICATE USERNAME
    @Test
    void shouldThrowRegisterUserAlreadyExistsException() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("username");
        request.setPassword("password");

        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () ->
                authService.register(request)
        );
    }

    // ❌ 3. NULL REQUEST
    @Test
    void shouldThrowExceptionWhenRequestIsNull() {

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(null)
        );
    }

    // ❌ 4. NULL USERNAME
    @Test
    void shouldThrowExceptionWhenRegisterUsernameIsNull() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername(null);
        request.setPassword("password");

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(request)
        );
    }

    // ❌ 5. EMPTY USERNAME
    @Test
    void shouldThrowExceptionWhenRegisterUsernameIsEmpty() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("");
        request.setPassword("password");

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(request)
        );
    }

    // ❌ 6. Blank Username(spaces)
    @Test
    void shouldThrowExceptionWhenRegisterUsernameIsBlank() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("   ");
        request.setPassword("password");

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(request)
        );
    }

    // ❌ 7. NULL PASSWORD
    @Test
    void shouldThrowExceptionWhenRegisterPasswordIsNull() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("username");
        request.setPassword(null);

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(request)
        );
    }

    // ❌ 8. EMPTY PASSWORD
    @Test
    void shouldThrowExceptionWhenRegisterPasswordIsEmpty() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("username");
        request.setPassword("");

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(request)
        );
    }
    @Test
    void shouldThrowExceptionWhenRegisterPasswordIsBlank() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("username");
        request.setPassword("   ");

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(request)
        );
    }
    //Login Test Cases 3 
    /* username testcase
     * password testcase
     * success testcase
     */
    
    
    //success testcase
    @Test
    void shouldLoginSuccessfully() {

        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("password");

        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setRole("ROLE_USER");

        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken("username", "ROLE_USER"))
                .thenReturn("token");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("token", response.getToken());
    }
    
    //username testcases
    
    // USER NOT FOUND
    
    @Test
    void shouldThrowExceptionWhenLoginUserNotFound() {

        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("password");

        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () ->
                authService.login(request)
        );
    }
    
    
    //NULL USERNAME
    
    
    @Test
    void shouldThrowExceptionWhenLoginUsernameIsNull() {

        LoginRequest request = new LoginRequest();
        request.setUsername(null);
        request.setPassword("password");

        assertThrows(InvalidCredentialsException.class, () ->
                authService.login(request)
        );
    }
    
    //EMPTY USERNAME
    
    
    @Test
    void shouldThrowExceptionWhenLoginUsernameIsEmpty() {

        LoginRequest request = new LoginRequest();
        request.setUsername("");
        request.setPassword("password");

        assertThrows(InvalidCredentialsException.class, () ->
                authService.login(request)
        );
    }
    
    //Blank Username
    
    @Test
    void shouldThrowExceptionWhenLoginUsernameIsBlank() {

        LoginRequest request = new LoginRequest();
        request.setUsername("   ");
        request.setPassword("password");

        assertThrows(InvalidCredentialsException.class, () ->
                authService.login(request)
        );
    }
    
   //Password TestCases
    
    //NULL PASSWORD
    
    @Test
    void shouldThrowExceptionWhenLoginPasswordIsNull() {

        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword(null);

        assertThrows(InvalidCredentialsException.class, () ->
                authService.login(request)
        );
    }
    
    //EMPTY PASSWORD
    @Test
    void shouldThrowExceptionWhenLoginPasswordIsEmpty() {

        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("");

        assertThrows(InvalidCredentialsException.class, () ->
                authService.login(request)
        );
    }
    
    //BLANK PASSWORD (spaces)
    
    @Test
    void shouldThrowExceptionWhenLoginPasswordIsBlank() {

        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("   ");

        assertThrows(InvalidCredentialsException.class, () ->
                authService.login(request)
        );
    }
    
    //Wrong Password
    @Test
    void shouldThrowExceptionWhenLoginPasswordIsWrong() {

        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("wrong");

        User user = new User();
        user.setUsername("username");
        user.setPassword("password"); // correct password

        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class, () ->
                authService.login(request)
        );
    }
}