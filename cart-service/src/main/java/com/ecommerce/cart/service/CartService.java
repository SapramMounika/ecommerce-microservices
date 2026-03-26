package com.ecommerce.cart.service;

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

public interface CartService {
	 // ✅ USER OPERATIONS

    // 1. Add item to cart
    ApiResponse<AddToCartResponse> addItemToCart(Long userId, AddToCartRequest request);

    // 2. Update item
    ApiResponse<UpdateCartItemResponse> updateCartItem(Long userId, UpdateCartItemRequest request);

    // 3. Remove item
    ApiResponse<RemoveCartItemResponse> removeCartItem(Long userId, RemoveCartItemRequest request);

    // 4. View cart
    ApiResponse<ViewCartResponse> viewCart(Long userId);

    // 5. Clear cart
    ApiResponse<ClearCartResponse> clearCart(Long userId);


    // ✅ ADMIN OPERATIONS

    // 6. View any user's cart
    ApiResponse<AdminViewCartResponse> getCartByUserId( AdminViewCartRequest request);

    // 7. Clear any user's cart
    ApiResponse<AdminClearCartResponse> clearCartByUserId(AdminClearCartRequest request);
   

  
}
