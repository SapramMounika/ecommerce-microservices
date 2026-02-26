# 🔐 JWT Authentication & Authorization – End-to-End Documentation

---

# 1️⃣ Overview

JWT (JSON Web Token) is implemented in the User Service to provide:

- Stateless authentication
- Role-based authorization
- Secure API access
- Scalability without session storage

This implementation ensures the service remains stateless and horizontally scalable.

---

# 2️⃣ Why JWT?

Traditional session-based authentication:

- Stores session in memory
- Not scalable in microservices
- Requires sticky sessions or shared session storage

JWT advantages:

- Stateless
- Self-contained
- Encodes user identity + role
- Easily verifiable
- Scalable across microservices

---

# 3️⃣ Authentication Flow (End-to-End)

1. User sends login request (username + password)
2. Service validates credentials
3. JWT token is generated
4. Token returned to client
5. Client sends token in Authorization header
6. JwtFilter validates token
7. SecurityContext is populated
8. Role-based authorization is applied

---

# 4️⃣ JWT Structure

A JWT has 3 parts:

Header.Payload.Signature

Example:
# 🔐 JWT Authentication & Authorization – End-to-End Documentation

---

# 1️⃣ Overview

JWT (JSON Web Token) is implemented in the User Service to provide:

- Stateless authentication
- Role-based authorization
- Secure API access
- Scalability without session storage

This implementation ensures the service remains stateless and horizontally scalable.

---

# 2️⃣ Why JWT?

Traditional session-based authentication:

- Stores session in memory
- Not scalable in microservices
- Requires sticky sessions or shared session storage

JWT advantages:

- Stateless
- Self-contained
- Encodes user identity + role
- Easily verifiable
- Scalable across microservices

---

# 3️⃣ Authentication Flow (End-to-End)

1. User sends login request (username + password)
2. Service validates credentials
3. JWT token is generated
4. Token returned to client
5. Client sends token in Authorization header
6. JwtFilter validates token
7. SecurityContext is populated
8. Role-based authorization is applied

---

# 4️⃣ JWT Structure

A JWT has 3 parts:

Header.Payload.Signature

Example:
# 🔐 JWT Authentication & Authorization – End-to-End Documentation

---

# 1️⃣ Overview

JWT (JSON Web Token) is implemented in the User Service to provide:

- Stateless authentication
- Role-based authorization
- Secure API access
- Scalability without session storage

This implementation ensures the service remains stateless and horizontally scalable.

---

# 2️⃣ Why JWT?

Traditional session-based authentication:

- Stores session in memory
- Not scalable in microservices
- Requires sticky sessions or shared session storage

JWT advantages:

- Stateless
- Self-contained
- Encodes user identity + role
- Easily verifiable
- Scalable across microservices

---

# 3️⃣ Authentication Flow (End-to-End)

1. User sends login request (username + password)
2. Service validates credentials
3. JWT token is generated
4. Token returned to client
5. Client sends token in Authorization header
6. JwtFilter validates token
7. SecurityContext is populated
8. Role-based authorization is applied

---

# 4️⃣ JWT Structure

A JWT has 3 parts:

Header.Payload.Signature

Example:

xxxxx.yyyyy.zzzzz

---

## Header

Contains:

```json
{
  "alg": "HS256",
  "typ": "JWT"
}


---

Payload

Contains:

{
  "sub": "mounika",
  "role": "ROLE_ADMIN",
  "iat": 1710000000,
  "exp": 1710003600
}

Fields:

| Field | Meaning         |
| ----- | --------------- |
| sub   | Username        |
| role  | User role       |
| iat   | Issued at       |
| exp   | Expiration time |

Signature

Created using:

Secret key

HS256 algorithm

Ensures token integrity.

5️⃣ Implementation Components
5.1 JwtUtil

Responsibilities:

Generate token

Extract username

Extract role

Validate token

Parse claims

Token Generation
generateToken(username, role)

Sets subject

Adds role claim

Sets issued time

Sets expiration time

Signs using secret key

5.2 JwtFilter

Extends:

OncePerRequestFilter

Responsibilities:

Extract Authorization header

Validate Bearer token

Parse username and role

Create Authentication object

Set SecurityContext

This ensures every request is authenticated before reaching controller.

5.3 SecurityConfig

Key configuration:

Disable CSRF

Permit /api/auth/**

Require authentication for other endpoints

Register JwtFilter before UsernamePasswordAuthenticationFilter

Example:

.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
6️⃣ Authorization Mechanism

Role-based access is implemented using:

@PreAuthorize

Examples:

@PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasAnyRole('ADMIN','USER')")

Spring Security checks role from SecurityContext populated by JwtFilter.

7️⃣ Login Flow Implementation
Step 1 – Client Sends Request
POST /api/auth/login

Body:

{
  "username": "mounika",
  "password": "password123"
}
Step 2 – Service Validates Credentials

Fetch user from database

Match password

If valid → generate JWT

Step 3 – Return Token
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
8️⃣ Using Token in API Calls

Client sends:

Authorization: Bearer <token>

Example:

GET /api/users/viewAll

Header:

Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
9️⃣ Token Expiration

Configured:

EXPIRATION_TIME = 60 * 60 * 1000;

Meaning:

1 hour validity

After expiration:

Token becomes invalid

Client must re-login

🔟 Error Handling

Invalid token:

→ 401 Unauthorized

Expired token:

→ 401 Unauthorized

Unauthorized role:

→ 403 Forbidden

GlobalExceptionHandler ensures structured error responses.

1️⃣1️⃣ Security Best Practices Applied

Stateless authentication

Secret key signing

Role-based access control

No session storage

DTO-based response

Token expiration enforcement

1️⃣2️⃣ Production Recommendations

For production:

Store secret key in environment variable

Use BCrypt password encryption

Enable HTTPS only

Rotate secret keys periodically

Implement refresh token mechanism

Add rate limiting

1️⃣3️⃣ Architecture Flow Diagram

Client
→ API Gateway
→ JwtFilter
→ SecurityContext
→ Controller
→ Service
→ Repository
→ Database

1️⃣4️⃣ Advantages of Current Implementation

Scalable

Microservice-friendly

No session storage

Clear role enforcement

Easy to extend for future services

1️⃣5️⃣ Future Enhancements

Refresh token implementation

Centralized authentication service

Token blacklist support

OAuth2 integration

Distributed cache validation

1️⃣6️⃣ Summary

The JWT implementation provides:

Secure authentication

Role-based authorization

Stateless architecture

Microservice scalability

Production-aligned security structure
