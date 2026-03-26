
package com.ecommerce.cart.controller;

import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.cart.dto.AddToCartRequest;
import com.ecommerce.cart.dto.AddToCartResponse;
import com.ecommerce.cart.dto.AdminClearCartRequest;
import com.ecommerce.cart.dto.AdminClearCartResponse;
import com.ecommerce.cart.dto.AdminViewCartRequest;
import com.ecommerce.cart.dto.AdminViewCartResponse;
import com.ecommerce.cart.dto.ClearCartResponse;
import com.ecommerce.cart.dto.RemoveCartItemRequest;
import com.ecommerce.cart.dto.RemoveCartItemResponse;
import com.ecommerce.cart.dto.UpdateCartItemRequest;
import com.ecommerce.cart.dto.UpdateCartItemResponse;
import com.ecommerce.cart.dto.ViewCartResponse;
import com.ecommerce.cart.response.ApiResponse;
import com.ecommerce.cart.service.CartService;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // =====================================================
    // ✅ ADD ITEM TO CART (USER)
    // =====================================================
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<AddToCartResponse>> addToCart(
            @RequestHeader("X-User-Id") Long userId,   // 🔥 From Gateway
            @Valid @RequestBody AddToCartRequest request) {

        ApiResponse<AddToCartResponse> response =
                cartService.addItemToCart(userId, request);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ✅ UPDATE CART ITEM (USER)
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
    // ✅ REMOVE ITEM FROM CART (USER)
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
    // ✅ VIEW CART (USER)
    // =====================================================
    @GetMapping("/view")
    public ResponseEntity<ApiResponse<ViewCartResponse>> viewCart(
            @RequestHeader("X-User-Id") Long userId) {

        ApiResponse<ViewCartResponse> response =
                cartService.viewCart(userId);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ✅ CLEAR CART (USER)
    // =====================================================
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<ClearCartResponse>> clearCart(
            @RequestHeader("X-User-Id") Long userId) {

        ApiResponse<ClearCartResponse> response =
                cartService.clearCart(userId);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // 🔐 ADMIN - VIEW ANY USER CART
    // =====================================================
    @GetMapping("/admin/view")
    public ResponseEntity<ApiResponse<AdminViewCartResponse>> viewUserCart(
            @RequestHeader("X-User-Role") String role,
            @RequestParam("userId") Long userId) {

        if (!role.equals("ADMIN") && !role.equals("ROLE_ADMIN")) {
            throw new RuntimeException("Access Denied");
        }

        AdminViewCartRequest request = new AdminViewCartRequest();
        request.setUserId(userId);

        ApiResponse<AdminViewCartResponse> response =
                cartService.getCartByUserId(request);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // 🔐 ADMIN - CLEAR ANY USER CART
    // =====================================================
    @DeleteMapping("/admin/clear")
    public ResponseEntity<ApiResponse<AdminClearCartResponse>> clearUserCart(
            @RequestHeader("X-User-Role") String role,
            @RequestParam("userId") Long userId) {

        if (!role.equals("ADMIN") && !role.equals("ROLE_ADMIN")) {
            throw new RuntimeException("Access Denied");
        }

        AdminClearCartRequest request = new AdminClearCartRequest();
        request.setUserId(userId);

        ApiResponse<AdminClearCartResponse> response =
                cartService.clearCartByUserId(request);

        return ResponseEntity.ok(response);
    }
}