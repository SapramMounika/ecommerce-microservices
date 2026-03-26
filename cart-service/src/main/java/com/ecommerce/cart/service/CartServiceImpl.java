package com.ecommerce.cart.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ecommerce.cart.client.ProductClient;
import com.ecommerce.cart.dto.*;
import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.cart.exception.*;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.cart.response.ApiResponse;
import com.ecommerce.cart.response.ApiResponseBuilder;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductClient productClient;
    
    public CartServiceImpl(CartRepository cartRepository,
            ProductClient productClient) {
this.cartRepository = cartRepository;
this.productClient = productClient;
}
    
    // 1️ ADD ITEM TO CART
    
    @Override
    public ApiResponse<AddToCartResponse> addItemToCart(Long userId, AddToCartRequest request) {

        // 1. Fetch existing cart OR create new cart for user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return newCart;
                });

        // 2. Validate: prevent duplicate product in cart
        boolean exists = cart.getItems().stream()
                .anyMatch(item -> item.getProductId().equals(request.getProductId()));

        if (exists) {
            throw new CartItemAlreadyExistsException("Product already exists in cart");
        }

        // 3. Create new CartItem
        CartItem item = new CartItem();
        item.setProductId(request.getProductId());
        item.setQuantity(request.getQuantity());

        //  NOTE: In real-world, fetch price from Product Service
        ProductResponse product =
                productClient.getProductById(request.getProductId()).getData();

        item.setPrice(product.getPrice());

        item.setPrice(product.getPrice());

        item.setCart(cart);

        // 4. Add item to cart
        cart.getItems().add(item);

        // 5. Save cart (cascade will save CartItem)
        Cart savedCart = cartRepository.save(cart);

        // 6. Build response DTO
        AddToCartResponse response = new AddToCartResponse();
        response.setCartId(savedCart.getId());
        response.setUserId(savedCart.getUserId());

        List<AddToCartResponse.Item> items = savedCart.getItems().stream().map(i -> {
            AddToCartResponse.Item dto = new AddToCartResponse.Item();
            dto.setCartItemId(i.getId());
            dto.setProductId(i.getProductId());
            dto.setQuantity(i.getQuantity());
            dto.setPrice(i.getPrice());
            dto.setTotalPrice(i.getPrice() * i.getQuantity());
            return dto;
        }).toList();

        response.setItems(items);
        response.setTotalAmount(items.stream()
                .mapToDouble(AddToCartResponse.Item::getTotalPrice)
                .sum());

        // 7. Return API response
        return new ApiResponse<>(
                true,
                "Item successfully added to cart",
                response,
                200,
                LocalDateTime.now()
        );
    }

    
    // 2️ UPDATE CART ITEM (INCREMENT / DECREMENT)
    
    @Override
    public ApiResponse<UpdateCartItemResponse> updateCartItem(Long userId, UpdateCartItemRequest request) {

        // 1. Fetch cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        // 2. Find item in cart
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Item not found in cart"));

        // 3. Update quantity
        int newQty = item.getQuantity() + request.getQuantity();

        if (newQty <= 0) {
            throw new RuntimeException("Quantity cannot be zero or negative");
        }

        item.setQuantity(newQty);

        // 4. Save cart
        Cart savedCart = cartRepository.save(cart);

        // 5. Build response
        UpdateCartItemResponse response = new UpdateCartItemResponse();
        response.setCartId(savedCart.getId());
        response.setUserId(savedCart.getUserId());

        List<UpdateCartItemResponse.Item> items = savedCart.getItems().stream().map(i -> {
            UpdateCartItemResponse.Item dto = new UpdateCartItemResponse.Item();
            dto.setCartItemId(i.getId());
            dto.setProductId(i.getProductId());
            dto.setQuantity(i.getQuantity());
            dto.setPrice(i.getPrice());
            dto.setTotalPrice(i.getPrice() * i.getQuantity());
            return dto;
        }).toList();

        response.setItems(items);
        response.setTotalAmount(items.stream()
                .mapToDouble(UpdateCartItemResponse.Item::getTotalPrice)
                .sum());

        
        
        return new ApiResponse<>(
                true,
                "Cart item quantity updated successfully",
                response,
                200,
                LocalDateTime.now()
        );
    }

  
    // 3️ REMOVE ITEM FROM CART
    
    @Override
    public ApiResponse<RemoveCartItemResponse> removeCartItem(Long userId, RemoveCartItemRequest request) {

        // 1. Fetch cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        // 2. Find item
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Item not found"));

        // 3. Remove item
        cart.getItems().remove(item);

        // 4. Save
        Cart savedCart = cartRepository.save(cart);

        // 5. Build response
        RemoveCartItemResponse response = new RemoveCartItemResponse();
        response.setCartId(savedCart.getId());
        response.setUserId(savedCart.getUserId());

        List<RemoveCartItemResponse.Item> items = savedCart.getItems().stream().map(i -> {
            RemoveCartItemResponse.Item dto = new RemoveCartItemResponse.Item();
            dto.setCartItemId(i.getId());
            dto.setProductId(i.getProductId());
            dto.setQuantity(i.getQuantity());
            dto.setPrice(i.getPrice());
            dto.setTotalPrice(i.getPrice() * i.getQuantity());
            return dto;
        }).toList();

        response.setItems(items);
        response.setTotalAmount(items.stream()
                .mapToDouble(RemoveCartItemResponse.Item::getTotalPrice)
                .sum());

        return new ApiResponse<>(
                true,
                "Item successfully removed from cart",
                response,
                200,
                LocalDateTime.now()
        );
    }

    
    // 4️ VIEW CART
    
    @Override
    public ApiResponse<ViewCartResponse> viewCart(Long userId) {

        // 1. Fetch cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        // 2. Build response
        ViewCartResponse response = new ViewCartResponse();
        response.setCartId(cart.getId());
        response.setUserId(cart.getUserId());

        List<ViewCartResponse.Item> items = cart.getItems().stream().map(i -> {
            ViewCartResponse.Item dto = new ViewCartResponse.Item();
            dto.setCartItemId(i.getId());
            dto.setProductId(i.getProductId());
            dto.setQuantity(i.getQuantity());
            dto.setPrice(i.getPrice());
            dto.setTotalPrice(i.getPrice() * i.getQuantity());
            return dto;
        }).toList();

        response.setItems(items);
        response.setTotalAmount(items.stream()
                .mapToDouble(ViewCartResponse.Item::getTotalPrice)
                .sum());

     
        
        
        return new ApiResponse<>(
                true,
                "Cart fetched successfully",
                response,
                200,
                LocalDateTime.now()
        );
    }

    
    // 5️ CLEAR CART
   
    @Override
    public ApiResponse<ClearCartResponse> clearCart(Long userId) {

        // 1. Fetch cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        // 2. Validate cart is not empty
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new CartEmptyException("Cart is already empty");
        }

        // 3. Clear items
        cart.getItems().clear();

        // 4. Save cart
        Cart savedCart = cartRepository.save(cart);

        // 5. Build response
        ClearCartResponse response = new ClearCartResponse();
        response.setCartId(savedCart.getId());
        response.setUserId(savedCart.getUserId());
        response.setItems(List.of());
        response.setTotalAmount(0.0);

        
        
        
        return new ApiResponse<>(
                true,
                "Cart cleared successfully",
                response,
                200,
                LocalDateTime.now()
        );
    }


    // 6️ ADMIN - VIEW ANY USER CART
    
    @Override
    public ApiResponse<AdminViewCartResponse> getCartByUserId(AdminViewCartRequest request) {

        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        AdminViewCartResponse response = new AdminViewCartResponse();
        response.setCartId(cart.getId());
        response.setUserId(cart.getUserId());

        List<AdminViewCartResponse.Item> items = cart.getItems().stream().map(i -> {
            AdminViewCartResponse.Item dto = new AdminViewCartResponse.Item();
            dto.setCartItemId(i.getId());
            dto.setProductId(i.getProductId());
            dto.setQuantity(i.getQuantity());
            dto.setPrice(i.getPrice());
            dto.setTotalPrice(i.getPrice() * i.getQuantity());
            return dto;
        }).toList();

        response.setItems(items);
        response.setTotalAmount(items.stream()
                .mapToDouble(AdminViewCartResponse.Item::getTotalPrice)
                .sum());

        
        
        return new ApiResponse<>(
                true,
                "Admin successfully fetched user cart",
                response,
                200,
                LocalDateTime.now()
        );
    }

    
    // 7️ ADMIN - CLEAR ANY USER CART
    
    @Override
    public ApiResponse<AdminClearCartResponse> clearCartByUserId(AdminClearCartRequest request) {

        // 1. Fetch cart
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        // 2. Check if empty
        if (cart.getItems().isEmpty()) {
            throw new CartEmptyException("Cart is already empty");
        }

        // 3. Clear items
        cart.getItems().clear();

        // 4. Save cart
        Cart savedCart = cartRepository.save(cart);

        // 5. Build response
        AdminClearCartResponse response = new AdminClearCartResponse();
        response.setCartId(savedCart.getId());
        response.setUserId(savedCart.getUserId());
        response.setItems(List.of());
        response.setTotalAmount(0.0);

        // 6. Return
       
        return new ApiResponse<>(
                true,
                "Admin successfully cleared user cart",
                response,
                200,
                LocalDateTime.now()
        );
    }
  

   
}