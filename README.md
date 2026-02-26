# 🛒 E-Commerce Microservices Backend

A scalable microservices-based backend application built using Spring Boot and Spring Cloud.

This project follows enterprise-level architecture patterns including centralized configuration management, service discovery, API Gateway routing, JWT-based authentication, and role-based authorization.

---

# 📌 Architecture Overview

The system follows a microservices architecture with the following components:

## 🔹 Config Server
Centralized configuration management for all microservices.

- Spring Cloud Config Server
- External configuration repository support
- Centralized property management

## 🔹 Eureka Discovery Server
Service registry for dynamic service discovery.

- Spring Cloud Netflix Eureka
- Automatic service registration
- Health monitoring

## 🔹 API Gateway
Single entry point for all client requests.

- Spring Cloud Gateway
- Centralized routing
- Future-ready for centralized JWT validation

## 🔹 User Service
Responsible for authentication and user management.

Features implemented:

- User Registration
- User Login
- JWT Token Generation
- Role-Based Authorization (ADMIN / USER)
- Secure CRUD operations
- Global Exception Handling
- Validation Layer
- Standard API Response Wrapper

---

# 🔐 Security Implementation

Authentication and authorization are implemented using:

- Spring Security
- JWT (JSON Web Token)
- Role-Based Access Control

## Roles Implemented

| Role       | Access |
|------------|--------|
| ADMIN      | Full CRUD access |
| USER       | View operations only |

## JWT Flow

1. User logs in
2. Token generated with username and role
3. Token sent in `Authorization` header
4. JWT filter validates token for protected endpoints

---

# 📂 Project Structure
# 🛒 E-Commerce Microservices Backend

A scalable microservices-based backend application built using Spring Boot and Spring Cloud.

This project follows enterprise-level architecture patterns including centralized configuration management, service discovery, API Gateway routing, JWT-based authentication, and role-based authorization.

---

# 📌 Architecture Overview

The system follows a microservices architecture with the following components:

## 🔹 Config Server
Centralized configuration management for all microservices.

- Spring Cloud Config Server
- External configuration repository support
- Centralized property management

## 🔹 Eureka Discovery Server
Service registry for dynamic service discovery.

- Spring Cloud Netflix Eureka
- Automatic service registration
- Health monitoring

## 🔹 API Gateway
Single entry point for all client requests.

- Spring Cloud Gateway
- Centralized routing
- Future-ready for centralized JWT validation

## 🔹 User Service
Responsible for authentication and user management.

Features implemented:

- User Registration
- User Login
- JWT Token Generation
- Role-Based Authorization (ADMIN / USER)
- Secure CRUD operations
- Global Exception Handling
- Validation Layer
- Standard API Response Wrapper

---

# 🔐 Security Implementation

Authentication and authorization are implemented using:

- Spring Security
- JWT (JSON Web Token)
- Role-Based Access Control

## Roles Implemented

| Role       | Access |
|------------|--------|
| ADMIN      | Full CRUD access |
| USER       | View operations only |

## JWT Flow

1. User logs in
2. Token generated with username and role
3. Token sent in `Authorization` header
4. JWT filter validates token for protected endpoints

---

# 📂 Project Structure
ecommerce-microservices/
│
├── config-server/
├── discovery-server/
├── api-gateway/
├── user-service/
└── .gitignore


Each service follows layered architecture:

controller → service → repository → database


---

# 🛠 Tech Stack

## Backend
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Cloud

## Microservices Components
- Spring Cloud Config
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway

## Security
- JWT Authentication
- Role-Based Access Control

## Database
- PostgreSQL

## Build Tool
- Maven

## Version Control
- Git & GitHub

---

# 📡 Implemented APIs (User Service)

## Authentication APIs

POST `/api/auth/register`  
POST `/api/auth/login`

## User CRUD APIs

GET `/api/users/viewAll`  
GET `/api/users/viewById/{id}`  
POST `/api/users/add`  
PUT `/api/users/update/{id}`  
DELETE `/api/users/delete/{id}`

---

# 🧠 Design Decisions

- Layered architecture for separation of concerns
- DTO usage to avoid exposing entity directly
- Global exception handling using `@ControllerAdvice`
- Validation using Jakarta Validation annotations
- Standardized API response structure
- Stateless authentication using JWT
- Microservices communication through service discovery

---

# 🚀 Future Enhancements (Planned)

- BCrypt password encryption
- Pagination and sorting support
- Audit fields (createdAt, updatedAt)
- Soft delete implementation
- Centralized JWT validation in API Gateway
- Product, Category, Cart, Order services
- Dockerization
- CI/CD integration
- Production-grade logging and monitoring

---

# 🎯 Project Status

✔ Microservices architecture established  
✔ Secure User Service implemented  
✔ JWT authentication working  
✔ Role-based authorization working  
✔ Gateway routing ready  
✔ Code version controlled in GitHub  

---

# 👩‍💻 Author

Mounika Sapram  
Backend Developer | Java | Spring Boot | Microservices

