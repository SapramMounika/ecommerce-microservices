# 📘USER SERVICE – PAGINATION, SORTING & FILTERING DOCUMENTATION

## 1. Objective

To implement scalable data retrieval in User Service using:

- Pagination
- Sorting
- Filtering
- Clean API response design
- Backward compatibility (existing APIs unchanged)

This ensures:

- Performance optimization
- Secure API usage
- Frontend-friendly responses
- Production-ready architecture

## 2. What We Implemented

We implemented two versions:

### 🔹 A) Basic Pagination (Spring Default)

#### Endpoint

```
GET /api/users/paginated
```

#### Supports:

- Pagination
- Sorting

#### Example

#### Endpoint
```
GET /api/users/paginated?page=0&size=2&sortBy=username&direction=asc
```
#### How It Works

Returns Spring’s built-in:

```
Page<UserGetResponse>

```

Spring automatically adds metadata like:

* content
* pageable
* totalElements
* totalPages
* first
* last
* size
* number

#### Sample Response

```json

{
  "content": [
    {
      "id": 5,
      "username": "laliath",
      "role": "ROLE_USER"
    }
  ],
  "totalElements": 8,
  "totalPages": 4,
  "number": 0,
  "size": 2,
  "first": true,
  "last": false
}

```

#### Pros

 ✔ Easy to implement  
 
 ✔ Uses Spring Data Page  
 
 ✔ Good for internal APIs

#### Cons

 ❌ Exposes Spring internal fields
 
 ❌ Contains unnecessary metadata
 
 ❌ Less clean API contract

### 🔹 B) Advanced Pagination (Production-Level)

We created a new endpoint without modifying the old one.

#### Endpoint

```
GET /api/users/search

```
## 3. Advanced Pagination Features

This endpoint supports:

✅ Pagination  

✅ Sorting

- ✅ Filtering by username
- ✅ Filtering by role
- ✅ Max page size validation
- ✅ Allowed sort field validation
- ✅ Custom clean response format

## 4. API Parameters


| Parameter | Description                        | Default |
| --------- | ---------------------------------- | ------- |
| page      | Page number (0-based)              | 0       |
| size      | Records per page                   | 5       |
| sortBy    | Field to sort (id, username, role) | id      |
| direction | asc / desc                         | asc     |
| username  | Optional filter                    | null    |
| role      | Optional filter                    | null    |


## 5️. Example Calls

#### Basic Pagination
```
GET /api/users/search?page=0&size=5

```
#### Sorting

```
GET /api/users/search?sortBy=username&direction=desc

```
#### Filtering by Username
```

GET /api/users/search?username=lal

```
#### Filtering by Role

```

GET /api/users/search?role=ROLE_ADMIN

```
#### Full Example

```
GET /api/users/search?page=0&size=5&sortBy=username&direction=asc&username=la&role=ROLE_USER
```
## 6️. Advanced Response Structure

Instead of returning Spring’s Page object, we created:
```
PageResponse<T>
```
#### Sample Response
```json
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
```
## 7. Why Advanced Pagination Is Production-Level

#### 1️⃣ Hides Framework Internals

We do not expose:

* pageable
* sort objects
* internal flags

#### 2️⃣ Controlled API Contract

We decide what client sees.

#### 3️⃣ Security Protection

We added:
```
int maxSize = 50;
```
Prevents:
```
size=100000
```
#### 4️⃣ Sort Field Validation

Only allows:

- id
- username
- role

Prevents malicious sorting.

#### 5️⃣ Clean Architecture

Flow:

Client
→ Controller
→ Service
→ Repository
→ Database
→ DTO Mapping
→ PageResponse
→ Client

## 8️. Database-Level Behavior

Spring automatically generates:

#### Query 1 – Fetch Page

```json

SELECT * FROM users
WHERE username ILIKE '%la%'
AND role ILIKE '%ROLE_USER%'
ORDER BY username ASC
LIMIT 5 OFFSET 0;
```
#### Query 2 – Count Total
```json

SELECT COUNT(*) FROM users;

```
This is how totalElements and totalPages are calculated.

## 9️. Backward Compatibility Maintained

We DID NOT modify:
```
GET /api/users/viewAll
GET /api/users/paginated
```
We only added:
```
GET /api/users/search
```
This ensures:

-✅ No breaking changes
-✅ Safe API evolution
-✅ Version-safe design

## 10. Comparison Summary

| Parameter | Description                        | Default |
| --------- | ---------------------------------- | ------- |
| page      | Page number (0-based)              | 0       |
| size      | Records per page                   | 5       |
| sortBy    | Field to sort (id, username, role) | id      |
| direction | asc / desc                         | asc     |
| username  | Optional filter                    | null    |
| role      | Optional filter                    | null    |

## 11. Architecture Value

With advanced pagination implemented, User Service now supports:

* Scalable data retrieval
* Dynamic filtering
* Safe sorting
* API contract control
This aligns with microservices best practices.

 ## 12. Summary 

I have implemented both basic and advanced pagination strategies. The advanced implementation includes secure sorting validation, filtering capabilities, max-size protection, and a custom PageResponse wrapper to hide framework internals and provide a clean, frontend-friendly API contract. This ensures scalability, security, and production readiness.

## 🎯 Current User Service Capabilities

- ✅ JWT Authentication  
- ✅ Role-Based Authorization
- ✅ Global Exception Handling
- ✅ Validation
- ✅ Basic Pagination
- ✅ Advanced Pagination
- ✅ Sorting
- ✅ Filtering
- ✅ Clean API Contract
