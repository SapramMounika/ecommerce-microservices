
package com.ecommerce.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import com.ecommerce.cart.dto.AddToCartRequest;
import com.ecommerce.cart.dto.AddToCartResponse;
import com.ecommerce.cart.dto.AdminClearCartRequest;
import com.ecommerce.cart.dto.AdminClearCartResponse;
import com.ecommerce.cart.dto.AdminViewAllCartsRequest;
import com.ecommerce.cart.dto.AdminViewAllCartsResponse;
import com.ecommerce.cart.dto.AdminViewCartByCartIdRequest;
import com.ecommerce.cart.dto.AdminViewCartByCartIdResponse;
import com.ecommerce.cart.dto.AdminViewCartByUserIdRequest;
import com.ecommerce.cart.dto.AdminViewCartByUserIdResponse;
import com.ecommerce.cart.dto.ClearMyCart;
import com.ecommerce.cart.dto.RemoveCartItemRequest;
import com.ecommerce.cart.dto.RemoveCartItemResponse;
import com.ecommerce.cart.dto.UpdateCartItemRequest;
import com.ecommerce.cart.dto.UpdateCartItemResponse;
import com.ecommerce.cart.dto.ViewMyCart;
import com.ecommerce.cart.response.ApiResponse;
import com.ecommerce.cart.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // =====================================================
    // USER 1: ADD ITEM TO CART
    // POST /api/cart/add
    // =====================================================

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<AddToCartResponse>> addToCart(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody AddToCartRequest request) {

        ApiResponse<AddToCartResponse> response =
                cartService.addItemToCart(userId, request);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // USER 2: UPDATE ITEM QUANTITY
    // PUT /api/cart/update
    // =====================================================

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UpdateCartItemResponse>> updateCartItem(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        ApiResponse<UpdateCartItemResponse> response =
                cartService.updateCartItem(userId, request);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // USER 3: REMOVE ITEM FROM CART
    // DELETE /api/cart/remove
    // =====================================================

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<RemoveCartItemResponse>> removeItem(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody RemoveCartItemRequest request) {

        ApiResponse<RemoveCartItemResponse> response =
                cartService.removeCartItem(userId, request);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // USER 4: VIEW MY CART
    // GET /api/cart/view
    // =====================================================

    @GetMapping("/view")
    public ResponseEntity<ApiResponse<ViewMyCart>> viewMyCart(
            @RequestHeader("X-User-Id") Long userId) {

        ApiResponse<ViewMyCart> response =
                cartService.viewCart(userId);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // USER 5: CLEAR MY CART
    // DELETE /api/cart/clear
    // =====================================================

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<ClearMyCart>> clearMyCart(
            @RequestHeader("X-User-Id") Long userId) {

        ApiResponse<ClearMyCart> response =
                cartService.clearCart(userId);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ADMIN 1: VIEW ALL CARTS WITH PAGINATION
    // GET /api/cart/admin/all?page=0&size=10
    // =====================================================

    @GetMapping("/admin/all")
    public ResponseEntity<ApiResponse<AdminViewAllCartsResponse>> viewAllCarts(
            @RequestHeader("X-User-Role") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        checkAdmin(role);

        AdminViewAllCartsRequest request =
                new AdminViewAllCartsRequest();

        request.setPage(page);
        request.setSize(size);

        // Validate pagination bounds before querying the database.
        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page must be >= 0 and size must be between 1 and 100");
        }

        ApiResponse<AdminViewAllCartsResponse> response =
                cartService.getCartByUserId(request);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ADMIN 2: VIEW CART BY USER ID
    // GET /api/cart/admin/user?userId=1
    // =====================================================

    @GetMapping("/admin/user")
    public ResponseEntity<ApiResponse<AdminViewCartByUserIdResponse>>
            viewCartByUserId(
                    @RequestHeader("X-User-Role") String role,
                    @RequestParam Long userId) {

        checkAdmin(role);

        AdminViewCartByUserIdRequest request =
                new AdminViewCartByUserIdRequest();

        request.setUserId(userId);

        ApiResponse<AdminViewCartByUserIdResponse> response =
                cartService.getCartByUserId(request);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ADMIN 3: VIEW CART BY CART ID
    // GET /api/cart/admin/cart?cartId=1
    // =====================================================

    @GetMapping("/admin/cart")
    public ResponseEntity<ApiResponse<AdminViewCartByCartIdResponse>>
            viewCartByCartId(
                    @RequestHeader("X-User-Role") String role,
                    @RequestParam Long cartId) {

        checkAdmin(role);

        AdminViewCartByCartIdRequest request =
                new AdminViewCartByCartIdRequest();

        request.setCartId(cartId);

        ApiResponse<AdminViewCartByCartIdResponse> response =
                cartService.getCartByCartId(request);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ADMIN 4: CLEAR ANY USER'S CART
    // DELETE /api/cart/admin/clear?userId=1
    // =====================================================

    @DeleteMapping("/admin/clear")
    public ResponseEntity<ApiResponse<AdminClearCartResponse>>
            clearUserCart(
                    @RequestHeader("X-User-Role") String role,
                    @RequestParam Long userId) {

        checkAdmin(role);

        AdminClearCartRequest request = new AdminClearCartRequest();
        request.setUserId(userId);

        ApiResponse<AdminClearCartResponse> response =
                cartService.clearCartByUserId(request);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // PRIVATE METHOD: ADMIN AUTHORIZATION CHECK
    // =====================================================

    private void checkAdmin(String role) {
        if (role == null ||
                (!"ADMIN".equals(role) &&
                 !"ROLE_ADMIN".equals(role))) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied. Admin role required.");
        }
    }
}
