
package com.ecommerce.cart.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import com.ecommerce.cart.client.ProductClient;
import com.ecommerce.cart.dto.*;
import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.cart.exception.CartEmptyException;
import com.ecommerce.cart.exception.CartItemAlreadyExistsException;
import com.ecommerce.cart.exception.CartItemNotFoundException;
import com.ecommerce.cart.exception.CartNotFoundException;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.cart.response.ApiResponse;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductClient productClient;

    public CartServiceImpl(
            CartRepository cartRepository,
            ProductClient productClient) {
        this.cartRepository = cartRepository;
        this.productClient = productClient;
    }

    // =====================================================
    // USER 1: ADD ITEM TO CART
    // =====================================================

    @Override
    public ApiResponse<AddToCartResponse> addItemToCart(
            Long userId, AddToCartRequest request) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return newCart;
                });

        boolean alreadyExists = cart.getItems().stream()
                .anyMatch(item ->
                        item.getProductId().equals(request.getProductId()));

        if (alreadyExists) {
            throw new CartItemAlreadyExistsException(
                    "Product already exists in cart. Update its quantity.");
        }

        // Fetch product information from Product Service.
        var productResult =
                productClient.getProductById(request.getProductId());

        if (productResult == null || productResult.getData() == null) {
            throw new IllegalStateException(
                    "Product details could not be retrieved.");
        }

        ProductResponse product = productResult.getData();

        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new IllegalStateException("Invalid product price.");
        }

        CartItem item = new CartItem();
        item.setProductId(product.getProductId());
        item.setQuantity(request.getQuantity());
        item.setPrice(product.getPrice());
        item.setCart(cart);

        cart.getItems().add(item);

        Cart savedCart = cartRepository.save(cart);

        AddToCartResponse response = new AddToCartResponse();
        response.setCartId(savedCart.getId());
        response.setUserId(savedCart.getUserId());
        response.setItems(savedCart.getItems().stream()
                .map(this::toAddToCartItem)
                .toList());
        response.setTotalAmount(calculateTotal(savedCart));

        return success("Item added to cart successfully", response);
    }

    // =====================================================
    // USER 2: UPDATE ITEM QUANTITY
    // =====================================================

    @Override
    public ApiResponse<UpdateCartItemResponse> updateCartItem(
            Long userId, UpdateCartItemRequest request) {

        Cart cart = getCartByUserId(userId);

        CartItem item = cart.getItems().stream()
                .filter(i ->
                        i.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() ->
                        new CartItemNotFoundException(
                                "Product not found in cart."));

        // Set the new quantity supplied in the request.
        item.setQuantity(request.getQuantity());

        Cart savedCart = cartRepository.save(cart);

        UpdateCartItemResponse response = new UpdateCartItemResponse();
        response.setCartId(savedCart.getId());
        response.setUserId(savedCart.getUserId());

        List<UpdateCartItemResponse.Item> items =
                savedCart.getItems().stream()
                .map(i -> {
                    UpdateCartItemResponse.Item dto =
                            new UpdateCartItemResponse.Item();
                    dto.setCartItemId(i.getId());
                    dto.setProductId(i.getProductId());
                    dto.setQuantity(i.getQuantity());
                    dto.setPrice(i.getPrice());
                    dto.setTotalPrice(i.getPrice() * i.getQuantity());
                    return dto;
                })
                .toList();

        response.setItems(items);
        response.setTotalAmount(calculateTotal(savedCart));

        return success("Cart item quantity updated successfully", response);
    }

    // =====================================================
    // USER 3: REMOVE ITEM FROM CART
    // =====================================================

    @Override
    public ApiResponse<RemoveCartItemResponse> removeCartItem(
            Long userId, RemoveCartItemRequest request) {

        Cart cart = getCartByUserId(userId);

        CartItem item = cart.getItems().stream()
                .filter(i ->
                        i.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() ->
                        new CartItemNotFoundException(
                                "Product not found in cart."));

        cart.getItems().remove(item);

        // orphanRemoval=true on Cart.items removes the deleted item.
        Cart savedCart = cartRepository.save(cart);

        RemoveCartItemResponse response = new RemoveCartItemResponse();
        response.setCartId(savedCart.getId());
        response.setUserId(savedCart.getUserId());

        List<RemoveCartItemResponse.Item> items =
                savedCart.getItems().stream()
                .map(i -> {
                    RemoveCartItemResponse.Item dto =
                            new RemoveCartItemResponse.Item();
                    dto.setCartItemId(i.getId());
                    dto.setProductId(i.getProductId());
                    dto.setQuantity(i.getQuantity());
                    dto.setPrice(i.getPrice());
                    dto.setTotalPrice(i.getPrice() * i.getQuantity());
                    return dto;
                })
                .toList();

        response.setItems(items);
        response.setTotalAmount(calculateTotal(savedCart));

        return success("Cart item removed successfully", response);
    }

    // =====================================================
    // USER 4: VIEW MY CART
    // =====================================================

    @Override
    public ApiResponse<ViewMyCart> viewCart(Long userId) {

        Cart cart = getCartByUserId(userId);

        ViewMyCart response = new ViewMyCart();
        response.setCartId(cart.getId());
        response.setUserId(cart.getUserId());

        List<ViewMyCart.Item> items = cart.getItems().stream()
                .map(i -> {
                    ViewMyCart.Item dto = new ViewMyCart.Item();
                    dto.setCartItemId(i.getId());
                    dto.setProductId(i.getProductId());
                    dto.setQuantity(i.getQuantity());
                    dto.setPrice(i.getPrice());
                    dto.setTotalPrice(i.getPrice() * i.getQuantity());
                    return dto;
                })
                .toList();

        response.setItems(items);
        response.setTotalAmount(calculateTotal(cart));

        return success("Cart retrieved successfully", response);
    }

    // =====================================================
    // USER 5: CLEAR MY CART
    // =====================================================

    @Override
    public ApiResponse<ClearMyCart> clearCart(Long userId) {

        Cart cart = getCartByUserId(userId);

        if (cart.getItems().isEmpty()) {
            throw new CartEmptyException("Cart is already empty.");
        }

        cart.getItems().clear();
        Cart savedCart = cartRepository.save(cart);

        ClearMyCart response = new ClearMyCart();
        response.setCartId(savedCart.getId());
        response.setUserId(savedCart.getUserId());
        response.setItems(List.of());
        response.setTotalAmount(0.0);

        return success("Cart cleared successfully", response);
    }

    // =====================================================
    // ADMIN 1: VIEW ALL CARTS (PAGINATED)
    // =====================================================

    @Override
    public ApiResponse<AdminViewAllCartsResponse> getCartByUserId(
            AdminViewAllCartsRequest request) {

        Page<Cart> cartPage = cartRepository.findAll(
                PageRequest.of(request.getPage(), request.getSize()));

        List<AdminViewAllCartsResponse.CartDetails> carts =
                cartPage.getContent().stream()
                .map(cart -> {
                    AdminViewAllCartsResponse.CartDetails dto =
                            new AdminViewAllCartsResponse.CartDetails();

                    dto.setCartId(cart.getId());
                    dto.setUserId(cart.getUserId());
                    dto.setItems(cart.getItems().stream()
                            .map(this::toAddToCartItem)
                            .toList());

                    dto.setTotalItems(cart.getItems().stream()
                            .mapToInt(CartItem::getQuantity)
                            .sum());

                    dto.setTotalAmount(calculateTotal(cart));
                    return dto;
                })
                .toList();

        AdminViewAllCartsResponse response =
                new AdminViewAllCartsResponse();

        response.setCarts(carts);
        response.setPage(cartPage.getNumber());
        response.setSize(cartPage.getSize());
        response.setTotalElements(cartPage.getTotalElements());
        response.setTotalPages(cartPage.getTotalPages());
        

        return success("All carts retrieved successfully", response);
    }

    // =====================================================
    // ADMIN 2: CLEAR A USER'S CART
    // =====================================================

    @Override
    public ApiResponse<AdminClearCartResponse> clearCartByUserId(
            AdminClearCartRequest request) {

        Cart cart = getCartByUserId(request.getUserId());

        if (cart.getItems().isEmpty()) {
            throw new CartEmptyException("Cart is already empty.");
        }

        cart.getItems().clear();
        Cart savedCart = cartRepository.save(cart);

        AdminClearCartResponse response = new AdminClearCartResponse();
        response.setCartId(savedCart.getId());
        response.setUserId(savedCart.getUserId());
        response.setMessage("User cart cleared successfully");

        return success("Admin cleared the user's cart successfully", response);
    }

    // =====================================================
    // ADMIN 3: VIEW CART BY USER ID
    // =====================================================

    @Override
    public ApiResponse<AdminViewCartByUserIdResponse> getCartByUserId(
            AdminViewCartByUserIdRequest request) {

        Cart cart = getCartByUserId(request.getUserId());

        AdminViewCartByUserIdResponse response =
                new AdminViewCartByUserIdResponse();

        response.setUserId(cart.getUserId());
        response.setCartId(cart.getId());

        List<AdminViewCartByUserIdResponse.Item> items =
                cart.getItems().stream()
                .map(i -> {
                    AdminViewCartByUserIdResponse.Item dto =
                            new AdminViewCartByUserIdResponse.Item();
                    dto.setCartItemId(i.getId());
                    dto.setProductId(i.getProductId());
                    dto.setQuantity(i.getQuantity());
                    dto.setPrice(i.getPrice());
                    dto.setTotalPrice(i.getPrice() * i.getQuantity());
                    return dto;
                })
                .toList();

        response.setItems(items);
        response.setTotalItems(cart.getItems().stream()
                .mapToInt(CartItem::getQuantity).sum());
        response.setTotalAmount(calculateTotal(cart));
        

        return success("Cart retrieved by user ID successfully", response);
    }

    // =====================================================
    // ADMIN 4: VIEW CART BY CART ID
    // =====================================================

    @Override
    public ApiResponse<AdminViewCartByCartIdResponse> getCartByCartId(
            AdminViewCartByCartIdRequest request) {

        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() ->
                        new CartNotFoundException("Cart not found."));

        AdminViewCartByCartIdResponse response =
                new AdminViewCartByCartIdResponse();

        response.setCartId(cart.getId());
        response.setUserId(cart.getUserId());

        List<AdminViewCartByCartIdResponse.Item> items =
                cart.getItems().stream()
                .map(i -> {
                    AdminViewCartByCartIdResponse.Item dto =
                            new AdminViewCartByCartIdResponse.Item();
                    dto.setCartItemId(i.getId());
                    dto.setProductId(i.getProductId());
                    dto.setQuantity(i.getQuantity());
                    dto.setPrice(i.getPrice());
                    dto.setTotalPrice(i.getPrice() * i.getQuantity());
                    return dto;
                })
                .toList();

        response.setItems(items);
        response.setTotalItems(cart.getItems().stream()
                .mapToInt(CartItem::getQuantity).sum());
        response.setTotalAmount(calculateTotal(cart));
        

        return success("Cart retrieved by cart ID successfully", response);
    }

    // =====================================================
    // PRIVATE HELPER METHODS
    // =====================================================

    private Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new CartNotFoundException("Cart not found."));
    }

    private double calculateTotal(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    private AddToCartResponse.Item toAddToCartItem(CartItem item) {
        AddToCartResponse.Item dto = new AddToCartResponse.Item();
        dto.setCartItemId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setTotalPrice(item.getPrice() * item.getQuantity());
        return dto;
    }

    private <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                message,
                data,
                200,
                LocalDateTime.now());
    }
}
