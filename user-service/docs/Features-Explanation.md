# User Service - Standard API Response Wrapper

## Overview

The User Service implements a Standard API Response Wrapper to ensure all API responses follow a consistent format for both successful and error responses.

This approach improves:

- API consistency across microservices
- Better frontend integration
- Centralized error handling
- Easier debugging
- Production-ready API design

All APIs in this service return responses in the following structure.

### Standard Response Structure

Every API response follows this format:

```json
{
  "success": true/false,
  "message": "Operation successful/failure",
  "data": {},
  "status": 200/400/500,
  "timestamp": "yyyy-mm-ddThh:mm:ss"
}

```
#### Response Fields

| Field     | Description                              |
| --------- | ---------------------------------------- |
| success   | Indicates whether the API call succeeded |
| message   | Human-readable message                   |
| data      | Actual response payload                  |
| status    | HTTP status code                         |
| timestamp | Time when response was generated         |

### Authentication APIs
#### Register User

Endpoint
```
POST /api/auth/register
```
###### Successful Response

```json

{
 "success": true,
 "message": "User registered successfully",
 "data": {
   "id": 10,
   "username": "john",
   "role": "ROLE_USER"
 },
 "status": 201,
 "timestamp": "2026-03-14T18:35:00"
}

```
##### Failure Response 

```json

{
 "success": false,
 "message": "Username already exists",
 "data": null,
 "status": 409,
 "timestamp": "2026-03-14T18:35:00"
}

```
#### Login API

Endpoint
```
POST /api/auth/login
```
##### Successful Login

```json
{
 "success": true,
 "message": "Login successful",
 "data": {
   "token": "jwt-token"
 },
 "status": 200,
 "timestamp": "2026-03-14T18:35:00"
}
```

##### Login Failure – Invalid Credentials

```json

{
 "success": false,
 "message": "Invalid username or password",
 "data": null,
 "status": 401,
 "timestamp": "2026-03-14T18:35:00"
}
```
### User CRUD APIs

#### Get All Users

Endpoint
```
GET /api/users/viewAll
```
##### Success Response

```json
{
 "success": true,
 "message": "Users fetched successfully",
 "data": [
   {
     "id": 1,
     "username": "john",
     "role": "ROLE_USER"
   }
 ],
 "status": 200,
 "timestamp": "2026-03-14T18:40:00"
}
```
#### Get User By ID

Endpoint
```
GET /api/users/viewById/{id}
```
##### Success Response
```json
{
 "success": true,
 "message": "User fetched successfully",
 "data": {
   "id": 1,
   "username": "john",
   "role": "ROLE_USER"
 },
 "status": 200,
 "timestamp": "2026-03-14T18:40:00"
}
```
##### Failure – User Not Found

```json
{
 "success": false,
 "message": "User not found",
 "data": null,
 "status": 404,
 "timestamp": "2026-03-14T18:40:00"
}
```
#### Create User (Admin Only)

Endpoint

POST /api/users/add

Success Response

{
 "success": true,
 "message": "User created successfully",
 "data": {
   "id": 11,
   "username": "alex",
   "role": "ROLE_USER"
 },
 "status": 201,
 "timestamp": "2026-03-14T18:45:00"
}

Failure – Access Denied

{
 "success": false,
 "message": "Access denied",
 "data": null,
 "status": 403,
 "timestamp": "2026-03-14T18:45:00"
}
#### Update User

Endpoint
```
PUT /api/users/update/{id}
```
##### Success Response

```json

{
 "success": true,
 "message": "User updated successfully",
 "data": {
   "id": 11,
   "username": "user_updated",
   "role": "ROLE_USER"
 },
 "status": 200,
 "timestamp": "2026-03-14T18:45:00"
}

```

#### Delete User

Endpoint

```
DELETE /api/users/delete/{id}
```
##### Success Response
```json
{
 "success": true,
 "message": "User deleted successfully",
 "data": {
   "id": 11
 },
 "status": 200,
 "timestamp": "2026-03-14T18:45:00"
}
```
### Validation Errors

Example validation error

```json

{
 "success": false,
 "message": "Validation failed",
 "data": {
   "username": "Username must not be blank",
   "password": "Password must not be blank"
 },
 "status": 400,
 "timestamp": "2026-03-14T18:45:00"
}
```
#### Benefits of Standard API Response Wrapper

- Consistent API contract
- Easier frontend integration
- Centralized error handling
- Cleaner controller logic
- Enterprise-grade API design

### Project Structure

userservice
 ├── controller
 │     AuthController
 │     UserController
 │
 ├── service
 │     AuthService
 │     UserService
 │
 ├── repository
 │     UserRepository
 │
 ├── entity
 │     User
 │
 ├── dto
 │     LoginRequest
 │     LoginResponse
 │     RegisterRequest
 │     RegisterResponse
 │
 ├── exception
 │     GlobalExceptionHandler
 │
 ├── response
 │     ApiResponse
 │     ApiResponseBuilder
 │
 ├── security
 │     SecurityConfig
 │
 └── filter
       JwtFilter

### Conclusion

The Standard API Response Wrapper ensures that all API responses in the User Service follow a consistent structure for both success and error responses.
This improves API usability, maintainability, and scalability across microservices.


















