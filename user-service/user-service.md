# 👤 User Service Documentation

## 1️⃣ Overview

The User Service is responsible for:

- User Registration
- User Authentication (JWT-based)
- Role-Based Authorization (ADMIN / USER)
- User CRUD Operations
- Advanced Pagination, Sorting, and Filtering

This service is part of the E-Commerce Microservices Architecture and is registered with Eureka and accessible through API Gateway.

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

### 🔐 Authentication
- Users authenticate using username + password.
- On successful login, a JWT token is generated.
- JWT contains:
  - username
  - role
  - expiration time

### 🔐 Authorization
Role-based access using:

- `@PreAuthorize("hasRole('ADMIN')")`
- `@PreAuthorize("hasAnyRole('ADMIN','USER')")`

### Role Permissions

| Operation            | ADMIN         | USER |
|------------          |--------       |--------|
| Register             | ✅           | ✅ |
| Login                | ✅           | ✅ |
| View All Users       | ✅           | ✅ |
| View User By ID      | ✅           | ✅ |
| Create User          | ✅           | ❌ |
| Update User          | ✅           | ❌ |
| Delete User          | ✅           | ❌ |

---

## 5️⃣ API Endpoints

---

### 🔹 5.1 Authentication APIs

#### Register

POST /api/auth/register

Request:

```json
{
  "username": "mounika",
  "password": "password123",
  "role": "ROLE_USER"
}

{
  "message": "User registered successfully"
}

#### Login

Request:

{
  "username": "mounika",
  "password": "password123"
}

Response:

{
  "token": "JWT_TOKEN"
}

🔹 5.2 User CRUD APIs
View All Users
GET /api/users/viewAll

Role: ADMIN, USER

View User By ID
GET /api/users/viewById/{id}

Role: ADMIN, USER

Create User
POST /api/users/add

Role: ADMIN only

Update User
PUT /api/users/update/{id}

Role: ADMIN only

Delete User
DELETE /api/users/delete/{id}

Role: ADMIN only

Response:

{
  "message": "User deleted successfully"
}


6️⃣ Pagination Implementation

Two pagination strategies are implemented.

🔹 6.1 Basic Pagination (Spring Default)
GET /api/users/paginated

Supports:

Pagination

Sorting

Example:

GET /api/users/paginated?page=0&size=5&sortBy=username&direction=asc

Returns Spring's Page<> object including:

content

totalElements

totalPages

first

last

🔹 6.2 Advanced Pagination (Production-Level)
GET /api/users/search

Supports:

Pagination

Sorting

Filtering by username

Filtering by role

Max page size validation

Sort field validation

Clean response wrapper

Parameters
Parameter	Description	Default
page	Page number (0-based)	0
size	Records per page	5
sortBy	id, username, role	id
direction	asc / desc	asc
username	Filter by username	optional
role	Filter by role	optional
Example
GET /api/users/search?page=0&size=5&sortBy=username&direction=asc&username=la&role=ROLE_USER
Response Format
{
  "data": [
    {
      "id": 5,
      "username": "laliath",
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
7️⃣ Production-Level Enhancements Implemented

DTO mapping (Entity not exposed)

Max page size limit (prevents abuse)

Allowed sort field validation

Null-safe filtering

Global exception handling

Clean API response format

Separation of concerns (Controller → Service → Repository)

8️⃣ Architecture Flow

Client
→ API Gateway
→ User Service Controller
→ Service Layer
→ Repository (Spring Data JPA)
→ PostgreSQL
→ DTO Mapping
→ PageResponse Wrapper
→ Client

9️⃣ Security Flow

User logs in.

JWT token is generated.

Client sends JWT in Authorization header.

JwtFilter validates token.

SecurityContext sets authentication.

@PreAuthorize controls access.

🔟 Scalability Considerations

Pagination prevents loading entire dataset.

Sorting handled at database level.

Filtering executed via indexed columns (recommended).

DTO prevents unnecessary data exposure.

1️⃣1️⃣ Future Improvements

BCrypt password encryption

Audit fields (createdAt, updatedAt)

Soft delete implementation

Redis caching

OpenAPI/Swagger documentation

Rate limiting

1️⃣2️⃣ Summary

The User Service is designed to be:

Secure

Scalable

Cleanly structured

Production-ready

Microservice compliant

It supports full authentication, authorization, CRUD operations, and advanced data retrieval mechanisms aligned with enterprise backend standards.
