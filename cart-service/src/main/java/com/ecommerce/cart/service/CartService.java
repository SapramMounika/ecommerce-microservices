package com.ecommerce.cart.service;

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

public interface CartService {
	 // ✅ USER OPERATIONS

    // 1. Add item to cart
    ApiResponse<AddToCartResponse> addItemToCart(Long userId, AddToCartRequest request);

    // 2. Update item
    ApiResponse<UpdateCartItemResponse> updateCartItem(Long userId, UpdateCartItemRequest request);

    // 3. Remove item
    ApiResponse<RemoveCartItemResponse> removeCartItem(Long userId, RemoveCartItemRequest request);

    // 4. View my cart
    ApiResponse<ViewMyCart> viewCart(Long userId);

    // 5. Clear cart
    ApiResponse<ClearMyCart> clearCart(Long userId);


    // ✅ ADMIN OPERATIONS

    // 1. View all user's cart
    ApiResponse<AdminViewAllCartsResponse> getCartByUserId( AdminViewAllCartsRequest request);

    // 2. Clear any user's cart
    ApiResponse<AdminClearCartResponse> clearCartByUserId(AdminClearCartRequest request);
   


    // 3. View cart by user ID
    ApiResponse<AdminViewCartByUserIdResponse> getCartByUserId(
            AdminViewCartByUserIdRequest request);

    // 4. View cart by cart ID
    ApiResponse<AdminViewCartByCartIdResponse> getCartByCartId(
            AdminViewCartByCartIdRequest request);
}
