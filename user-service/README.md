# 👤 User Service Documentation

## **1** Service Overview

The User Service is a core microservice in the E-Commerce Microservices Architecture.

It is responsible for:

- User Registration
- User Authentication (JWT-based)
- Role-Based Authorization (ADMIN / USER)
- User CRUD Operations
- Advanced Pagination, Sorting, and Filtering

This service integrates with:

- Eureka (Service Discovery)
- API Gateway (Centralized Routing)
- Config Server (Centralized Configuration)

---


## 🏗 Architecture Role

User Service is part of a Microservices Architecture:

Client → API Gateway → User Service → PostgreSQL

- Registered with Eureka
- Configuration managed via Config Server
- Secured using Spring Security + JWT

---

## 2️⃣ Tech Stack

- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA
- PostgreSQL
- Maven
- Eureka Client
  
---

## 3️⃣ Database Design

### Table: `users`

| Column   | Type     | Description |
|----------|----------|-------------|
| id       | Long     | Primary Key (Auto Increment) |
| username | String   | Unique username |
| password | String   | Encrypted password |
| role     | String   | ROLE_ADMIN / ROLE_USER |

---

## 4️⃣ Security Implementation

### 4.1 🔐 Authentication 
1. User sends login request.
2. Service validates credentials.
3. JWT token is generated.
4. Token contains:
   - username
   - role
   - expiration time
5. Token is returned to client.

### 4.2 🔐 Authorization 
1. Client sends JWT in Authorization header:
2. JwtFilter validates token.
3. SecurityContext is populated.
4. @PreAuthorize checks user role.
5. Access granted or denied.

### 4.3 Role Permissions

| Operation            | ADMIN        | USER |
|------------          |--------      |--------|
| Register             | ✅           | ✅ |
| Login                | ✅           | ✅ |
| View All Users       | ✅           | ✅ |
| View User By ID      | ✅           | ✅ |
| Create User          | ✅           | ❌ |
| Update User          | ✅           | ❌ |
| Delete User          | ✅           | ❌ |

---

## 5️⃣ API Documentation

---

### 🔹 5.1 Authentication APIs

#### Register

POST /api/auth/register

##### Request:

```json
{
  "username": "mounika",
  "password": "password123",
  "role": "ROLE_USER"
}
```
##### Response:

### Success Response (201)

```json
{
  "message": "User registered successfully"
}
```

### Error Response (400)

```json
{
  "timestamp": ".....",
  "status": 400,
  "message": "Username already exists"
}
```
#### Login

POST /api/auth/login

##### Request:

```json
{
  "username": "mounika",
  "password": "password123"
}

```
### Success Response (200):

```json
{
  "token": "JWT_TOKEN"
}

```
### Error Response (401):

```json
{
  "timestamp": "...",
  "status": 401,
  "message": "Invalid credentials"
}
```
### 🔹 5.2 User CRUD APIs

All endpoints require JWT authentication.

#### View All Users
```
GET /api/users/viewAll
```
Role: ADMIN, USER

#### View User By ID
```
GET /api/users/viewById/{id}
```
Role: ADMIN, USER

#### Create User
```
POST /api/users/add
```
Role: ADMIN only

#### Update User
```
PUT /api/users/update/{id}
```
Role: ADMIN only

#### Delete User
```
DELETE /api/users/delete/{id}
```
Role: ADMIN only

##### Response:

```json
{
  "message": "User deleted successfully"
}
```

## 6️⃣ Pagination Implementation

Two pagination strategies are implemented.

#### 🔹 6.1 Basic Pagination (Spring Default)
```
GET /api/users/paginated
```
Returns Spring Page<> object.
Used for internal or quick retrieval.

#### Supports:

- Pagination

- Sorting

#### Example:
```
GET /api/users/paginated?page=0&size=5&sortBy=username&direction=asc
```
Returns Spring's Page<> object including:

- content
- totalElements
- totalPages
- first
- last

#### 🔹 6.2 Advanced Pagination (Production-Level)
```
GET /api/users/search
```
#### Supports:

- Pagination
- Sorting
- Filtering by username
- Filtering by role
- Max page size validation
- Sort field validation
- Clean response wrapper

#### Query Parameters:

| Parameter | Default | Description      |
| --------- | ------- | ---------------- |
| page      | 0       | Page number      |
| size      | 5       | Records per page |
| sortBy    | id      | Sort field       |
| direction | asc     | Sort direction   |
| username  | null    | Username filter  |
| role      | null    | Role filter      |


#### Example
```
GET /api/users/search?page=0&size=5&sortBy=username&direction=asc&username=la&role=ROLE_USER
```
#### Response Format

```json
{
  "data": [
    {
      "id": 5,
      "username": "abc",
      "role": "ROLE_USER"
    }
  ],
  "pagination": {
    "currentPage": 0,
    "pageSize": 5,
    "totalElements": 8,
    "totalPages": 2,
    "isFirst": true,
    "isLast": false
  }
}

```
## 7️⃣ Error Handling Strategy

GlobalExceptionHandler ensures:

- Standardized error format
- Proper HTTP status codes
- Validation error handling

#### Status Codes:

| Code | Meaning               |
| ---- | --------------------- |
| 200  | Success               |
| 201  | Created               |
| 400  | Bad Request           |
| 401  | Unauthorized          |
| 403  | Forbidden             |
| 404  | Not Found             |
| 500  | Internal Server Error |

## 8️⃣ Configuration

application.properties

#### Important properties:

- Server port
- Database connection
- Eureka registration
- JWT secret key
- JWT expiration time

#### Example:
```
jwt.secret=yourVeryLongSecretKeyHere
jwt.expiration=3600000
```

## 9️⃣ Non-Functional Requirements

#### Performance

- Pagination prevents full table scans.
- Sorting executed at database level.
- Filtering uses indexed columns.

#### Scalability

- Stateless JWT authentication.
- Can be horizontally scaled.
- No session dependency.

#### Security

- Role-based authorization.
- Token expiration enforced.
- No entity exposure (DTO used).

## 🔟 Deployment Considerations

- Should run behind API Gateway.
- Direct service port access should be restricted in production.
- Environment variables recommended for JWT secret.
- Use Docker for containerization.

## 1️⃣1️⃣  Known Limitations

- No soft delete (currently hard delete).
- Password encryption may be basic (can upgrade to BCrypt).
- No rate limiting implemented.
- No audit fields (createdAt, updatedAt).


## 1️⃣2️⃣ Future Enhancements

- BCrypt encryption
- Audit fields
- Soft delete
- Redis caching
- OpenAPI/Swagger documentation
- Rate limiting
- Circuit breaker integration

## 8️⃣ Architecture Flow

Client  
↓  
API Gateway  
↓  
User Service Controller  
↓  
Service Layer  
↓  
Repository (Spring Data JPA)  
↓  
PostgreSQL  
↓  
DTO Mapping  
↓  
PageResponse Wrapper  
↓  
Client  


## 🔟 Scalability Considerations

- Pagination prevents loading entire dataset.
- Sorting handled at database level.
- Filtering executed via indexed columns (recommended).
- DTO prevents unnecessary data exposure.


## 1️⃣2️⃣ Summary

The User Service is designed to be:

- Secure
- Scalable
- Cleanly structured
- Production-ready
- Microservice compliant

It supports full authentication, authorization, CRUD operations, and advanced data retrieval mechanisms aligned with enterprise backend standards.
