# 🧪 User Service - Unit Testing Documentation

## 📌 Overview

This document describes the unit testing implementation for the **User Service** in the E-commerce Microservices application.

The testing is focused on validating:

- Business logic
- Input validation
- Exception handling
- Authentication (Login)
- Registration flow

---

## 🛠️ Technologies Used

- Java 17
- Spring Boot
- JUnit 5
- Mockito
- Maven

---

## 🧱 Project Structure
user-service
└── src
├── main/java/com/ecommerce/userservice
│ ├── service
│ │ └── AuthServiceImpl.java
│
└── test/java/com/ecommerce/userservice/service
└── AuthServiceTest.java


---

## 🎯 Testing Strategy

Unit testing is performed at the **Service Layer**.

### Key Goals:
- Validate business logic independently
- Avoid real database usage (mocked using Mockito)
- Ensure all edge cases are handled

---

# 🔐 AUTH SERVICE TESTING

## 1️⃣ REGISTER FUNCTIONALITY

### ✅ Success Scenario
- Valid username and password
- User is saved successfully

---

### ❌ Username Test Cases

| Test Case | Description |
|----------|-------------|
| Duplicate Username | User already exists in DB |
| Null Username | Username is null |
| Empty Username | Username is empty string |
| Blank Username | Username contains only spaces |

---

### ❌ Password Test Cases

| Test Case | Description |
|----------|-------------|
| Null Password | Password is null |
| Empty Password | Password is empty |
| Blank Password | Password contains only spaces |

---

### ❌ Other Cases

| Test Case | Description |
|----------|-------------|
| Null Request | Entire request object is null |

---

## 2️⃣ LOGIN FUNCTIONALITY

### ✅ Success Scenario
- Valid username and password
- JWT token generated successfully

---

### ❌ Username Test Cases

| Test Case | Description |
|----------|-------------|
| User Not Found | Username not present in DB |
| Null Username | Username is null |
| Empty Username | Username is empty |
| Blank Username | Username contains only spaces |

---

### ❌ Password Test Cases

| Test Case | Description |
|----------|-------------|
| Wrong Password | Incorrect password |
| Null Password | Password is null |
| Empty Password | Password is empty |
| Blank Password | Password contains only spaces |

---

## 🧪 Total Test Cases

- Register Tests: ~8
- Login Tests: ~10

👉 **Total: 18 Test Cases**

---

## 🧠 Key Concepts Covered

### 1. Validation Testing
- Null checks
- Empty values
- Blank inputs

---

### 2. Business Logic Testing
- Duplicate user detection
- User authentication
- Password validation

---

### 3. Exception Handling
- `UserAlreadyExistsException`
- `InvalidCredentialsException`
- `IllegalArgumentException`

---

### 4. Mocking
- Repository layer mocked using Mockito
- JWT token generation mocked

---

## 🔍 Sample Test Example

```java
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
📊 Test Execution Result

```
Runs: 18
Failures: 0
Errors: 0
```
✅ All test cases passed successfully

🔐 Security Consideration

For login:

Same error message is returned for both:

Username not found

Wrong password

👉 This prevents user enumeration attacks

🚀 Outcome

Service layer is fully tested

High reliability and stability

Ready for production-level standards

Strong foundation for further testing (Controller, Integration)

🔜 Next Steps

Controller Testing (MockMvc)

Integration Testing (Database)

Security Testing (JWT Filters)

End-to-End Testing

💬 Conclusion

Comprehensive unit testing ensures that the authentication system is:

Reliable

Secure

Maintainable

Production-ready




