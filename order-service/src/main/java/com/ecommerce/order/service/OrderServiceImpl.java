package com.ecommerce.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecommerce.order.dto.*;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.exception.*;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.response.ApiResponse;
import com.ecommerce.order.response.ApiResponseBuilder;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @Autowired
    private HttpServletRequest httpRequest;

    public OrderServiceImpl(OrderRepository orderRepository, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }

    // ===============================
    // 🛒 PLACE ORDER
    // ===============================
    @Override
    public ResponseEntity<ApiResponse<PlaceOrderResponse>> placeOrder(PlaceOrderRequest request) {

        // ✅ 1. Get userId from header
        String userHeader = httpRequest.getHeader("X-User-Id");
        if (userHeader == null) {
            throw new OrderServiceException("User header missing", 401);
        }

        Long userId = Long.parseLong(userHeader);

        // ✅ 2. Get token
        String token = httpRequest.getHeader("Authorization");

        System.out.println("USER ID: " + userId);
        System.out.println("TOKEN: " + token);

        // ===============================
        // 🛒 CALL CART SERVICE
        // ===============================
        ApiResponse<ViewCartResponse> cartResponse = webClient.get()
                .uri("http://localhost:8084/api/cart/view")
                .header("Authorization", token)
                .header("X-User-Id", userHeader)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<ViewCartResponse>>() {})
                .block();

        if (cartResponse == null || cartResponse.getData() == null ||
                cartResponse.getData().getItems() == null ||
                cartResponse.getData().getItems().isEmpty()) {
            throw new CartEmptyException();
        }

        List<CartItemDto> cartItems = cartResponse.getData().getItems();
        System.out.println("CART ITEMS: " + cartItems);

        // ===============================
        // 🧾 CREATE ORDER
        // ===============================
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        // ===============================
        // 📦 PROCESS ITEMS
        // ===============================
        List<OrderItem> items = cartItems.stream().map(cart -> {

            ApiResponse<ProductDto> productResponse = webClient.get()
                    .uri("http://localhost:8083/api/products/viewById/" + cart.getProductId())
                    .header("Authorization", token)
                    .header("X-User-Id", userHeader)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<ProductDto>>() {})
                    .block();

            ProductDto product = productResponse.getData();

            OrderItem item = new OrderItem();
            item.setProductId(cart.getProductId());
            item.setQuantity(cart.getQuantity());
            item.setPrice(product.getPrice());

            // ✅ 🔥 THIS LINE IS MISSING
            item.setOrder(order);

            return item;

        }).collect(Collectors.toList());

        // ===============================
        // 💰 CALCULATE TOTAL
        // ===============================
        double totalAmount = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        order.setItems(items);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // ===============================
        // 📤 RESPONSE
        // ===============================
        List<OrderItemResponse> itemResponses = savedOrder.getItems().stream().map(item -> {
            OrderItemResponse res = new OrderItemResponse();
            res.setProductId(item.getProductId());
            res.setQuantity(item.getQuantity());
            res.setPrice(item.getPrice());
            return res;
        }).collect(Collectors.toList());

        PlaceOrderResponse response = new PlaceOrderResponse();
        response.setOrderId(savedOrder.getOrderId());
        response.setUserId(savedOrder.getUserId());
        response.setTotalAmount(savedOrder.getTotalAmount());
        response.setStatus(savedOrder.getStatus().name());
        response.setCreatedAt(savedOrder.getCreatedAt());
        response.setItems(itemResponses);

        return ApiResponseBuilder.success("Order placed successfully", response, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<ApiResponse<List<GetAllOrdersResponse>>> getAllOrders(GetAllOrdersRequest request) {

        List<Order> orders = orderRepository.findAll();

        List<GetAllOrdersResponse> list = orders.stream().map(order -> {
            GetAllOrdersResponse res = new GetAllOrdersResponse();
            res.setOrderId(order.getOrderId());
            res.setUserId(order.getUserId());
            res.setTotalAmount(order.getTotalAmount());
            res.setStatus(order.getStatus().name());
            res.setCreatedAt(order.getCreatedAt());
            return res;
        }).toList();

        return ApiResponseBuilder.success("All orders fetched successfully", list, HttpStatus.OK);
    }
    // ===============================
    // 📄 GET ORDER BY ID
    // ===============================
    @Override
    public ResponseEntity<ApiResponse<GetOrderByIdResponse>> getOrderById(GetOrderByIdRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(request.getOrderId()));

        List<OrderItemResponse> items = order.getItems().stream().map(item -> {
            OrderItemResponse res = new OrderItemResponse();
            res.setProductId(item.getProductId());
            res.setQuantity(item.getQuantity());
            res.setPrice(item.getPrice());
            return res;
        }).collect(Collectors.toList());

        GetOrderByIdResponse response = new GetOrderByIdResponse();
        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUserId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus().name());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(items);

        return ApiResponseBuilder.success("Order fetched successfully", response, HttpStatus.OK);
    }

    // ===============================
    // 📄 GET MY ORDERS
    // ===============================
    @Override
    public ResponseEntity<ApiResponse<List<GetMyOrdersResponse>>> getMyOrders(GetMyOrdersRequest request) {

        String userHeader = httpRequest.getHeader("X-User-Id");
        if (userHeader == null) {
            throw new OrderServiceException("User header missing", 401);
        }

        Long userId = Long.parseLong(userHeader);

        List<Order> orders = orderRepository.findByUserId(userId);

        List<GetMyOrdersResponse> list = orders.stream().map(order -> {
            GetMyOrdersResponse res = new GetMyOrdersResponse();
            res.setOrderId(order.getOrderId());
            res.setTotalAmount(order.getTotalAmount());
            res.setStatus(order.getStatus().name());
            res.setCreatedAt(order.getCreatedAt());
            return res;
        }).collect(Collectors.toList());

        return ApiResponseBuilder.success("Orders fetched successfully", list, HttpStatus.OK);
    }

    // ===============================
    // ❌ CANCEL ORDER
    // ===============================
    @Override
    public ResponseEntity<ApiResponse<CancelOrderResponse>> cancelOrder(CancelOrderRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(request.getOrderId()));

        if (order.getStatus() == OrderStatus.SHIPPED ||
                order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOrderStateException("Cannot cancel shipped/delivered order");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        CancelOrderResponse response = new CancelOrderResponse();
        response.setOrderId(order.getOrderId());
        response.setStatus(order.getStatus().name());
        response.setMessage("Order cancelled successfully");

        return ApiResponseBuilder.success("Order cancelled", response, HttpStatus.OK);
    }
    
    
    
    
    
    @Override
    public ResponseEntity<ApiResponse<UpdateOrderStatusResponse>> updateOrderStatus(
            UpdateOrderStatusRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(request.getOrderId()));

        OrderStatus newStatus;

        try {
            newStatus = OrderStatus.valueOf(request.getStatus().toUpperCase());
        } catch (Exception e) {
            throw new InvalidOrderStateException("Invalid order status");
        }

        order.setStatus(newStatus);

        orderRepository.save(order);

        UpdateOrderStatusResponse response = new UpdateOrderStatusResponse();

        response.setOrderId(order.getOrderId());
        response.setStatus(order.getStatus().name());
        response.setMessage("Order status updated successfully");

        return ApiResponseBuilder.success(
                "Order status updated",
                response,
                HttpStatus.OK
        );
    }
}