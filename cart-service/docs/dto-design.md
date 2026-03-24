# 🛒 Cart Service DTO Design Documentation

## 📌 Overview

This document explains the **DTO (Data Transfer Object) design** for the Cart Service in the E-commerce microservices application.

The Cart Service handles operations such as:

* Add item to cart
* Update cart item (increment/decrement)
* Remove item from cart
* View cart
* Clear cart
* Admin operations (view/clear any user's cart)

---

## 🧠 Design Principles

### 1. Separation of Concerns

* **Entity classes** → Database mapping (Cart, CartItem)
* **DTO classes** → API request/response
* DTOs are **NOT directly tied to entities**

---

### 2. Standard API Response Wrapper

All APIs return a common structure:

```
ApiResponse<T>
```

```json
{
  "success": true,
  "message": "Operation successful",
  "status": 200,
  "timestamp": "2026-03-24T12:30:00",
  "data": { ... }
}
```

✔ Ensures consistency across all APIs
✔ Easy for frontend integration

---

### 3. Operation-Based DTO Design

DTOs are designed **per API operation**, not per entity.

| Operation   | Request DTO           | Response DTO           |
| ----------- | --------------------- | ---------------------- |
| Add Item    | AddToCartRequest      | AddToCartResponse      |
| Update Item | UpdateCartItemRequest | UpdateCartItemResponse |
| Remove Item | RemoveCartItemRequest | RemoveCartItemResponse |
| View Cart   | No request            | ViewCartResponse       |
| Clear Cart  | No request            | ClearCartResponse      |
| Admin View  | Optional userId       | AdminViewCartResponse  |
| Admin Clear | Optional userId       | AdminClearCartResponse |

---

## 🧱 Core Data Structure

### Cart Structure (Response)

```
Cart
 ├── cartId
 ├── userId
 ├── items (List<CartItem>)
 └── totalAmount
```

### CartItem Structure

```
CartItem
 ├── cartItemId
 ├── productId
 ├── quantity
 ├── price
 └── totalPrice
```

---

## 📥 Request DTOs

### 1. AddToCartRequest

```json
{
  "productId": 101,
  "quantity": 2
}
```

---

### 2. UpdateCartItemRequest

```json
{
  "productId": 101,
  "quantityChange": 1
}
```

✔ `+1` → increment
✔ `-1` → decrement

---

### 3. RemoveCartItemRequest

```json
{
  "productId": 101
}
```

---

### 4. View Cart Request

```http
GET /cart
```

✔ No request body
✔ User identified via JWT

---

### 5. Clear Cart Request

```http
DELETE /cart
```

✔ No request body

---

## 📤 Response DTO Structure

All responses follow:

```json
{
  "cartId": 1,
  "userId": 10,
  "items": [
    {
      "cartItemId": 5,
      "productId": 101,
      "quantity": 2,
      "price": 2500,
      "totalPrice": 5000
    }
  ],
  "totalAmount": 5000
}
```

---

## 🔄 Operation-wise Responses

### 1. Add to Cart

* Adds new item OR updates existing item
* Returns updated cart

---

### 2. Update Cart Item

* Adjusts quantity using `quantityChange`
* If quantity becomes 0 → item removed

---

### 3. Remove Item

* Deletes specific CartItem
* Cart remains

---

### 4. View Cart

* Returns full cart details
* No input required

---

### 5. Clear Cart

* Removes all items
* Returns empty cart

```json
"items": []
```

---

## 🧑‍💼 Admin Operations

### 1. View Any User Cart

```
GET /admin/cart/{userId}
```

---

### 2. Clear Any User Cart

```
DELETE /admin/cart/{userId}
```

---

## 🔐 Security Considerations

* User APIs → authenticated via JWT
* Admin APIs → restricted using role-based access

```
@PreAuthorize("hasRole('ADMIN')")
```

---

## ⚠️ Important Design Decisions

### 1. One Cart per User

* Each user has only one cart

---

### 2. One CartItem = One Product

* Multiple products → multiple CartItems

---

### 3. No Null Collections

* Always return:

```
items: []
```

❌ Never return null

---

### 4. Product Identification

* All operations use `productId`
* `cartItemId` is included only in response

---

### 5. Cart Persistence

* Cart is not deleted when empty
* Only items are cleared

---

## 🎯 Advantages of This Design

✔ Clean separation between entity and API
✔ Consistent response structure
✔ Scalable and maintainable
✔ Secure (JWT-based access)
✔ Frontend-friendly

---

## 🎤 Interview Explanation

> "We designed DTOs based on API use-cases rather than entities. Each operation has its own request DTO, while responses follow a consistent cart structure wrapped inside a standard ApiResponse. Cart acts as a container, and CartItem represents individual products. This approach ensures clean separation, scalability, and maintainability."

---

## ✅ Conclusion

This DTO design:

* Follows industry best practices
* Supports scalability and security
* Ensures consistency across APIs

---
