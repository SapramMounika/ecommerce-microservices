package com.ecommerce.order.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.order.dto.*;
import com.ecommerce.order.response.ApiResponse;
import com.ecommerce.order.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

   
    //  PLACE ORDER
   
    @PostMapping("/placeOrder")
    public ResponseEntity<ApiResponse<PlaceOrderResponse>> placeOrder(
            @RequestBody PlaceOrderRequest request) {

        return orderService.placeOrder(request);
    }

    
    //  GET ORDER BY ID
    
    @GetMapping("/get/{orderId}")
    public ResponseEntity<ApiResponse<GetOrderByIdResponse>> getOrderById(
            @PathVariable Long orderId) {

        GetOrderByIdRequest request = new GetOrderByIdRequest();
        request.setOrderId(orderId);

        return orderService.getOrderById(request);
    }

    
    //  GET MY ORDERS
   
    @GetMapping("/getMyOrders")
    public ResponseEntity<ApiResponse<List<GetMyOrdersResponse>>> getMyOrders() {
        return orderService.getMyOrders(new GetMyOrdersRequest());
    }

   
    //  GET ALL ORDERS (ADMIN)
    
    @GetMapping("/admin/getAllOrders")
    public ResponseEntity<ApiResponse<List<GetAllOrdersResponse>>> getAllOrders() {
        return orderService.getAllOrders(new GetAllOrdersRequest());
    }

    
    //  CANCEL ORDER
    
    @PutMapping("/cancel")
    public ResponseEntity<ApiResponse<CancelOrderResponse>> cancelOrder(
            @RequestBody CancelOrderRequest request) {

        return orderService.cancelOrder(request);
    }

    
    //  UPDATE ORDER STATUS (ADMIN)
    
    @PutMapping("/admin/status")
    public ResponseEntity<ApiResponse<UpdateOrderStatusResponse>> updateOrderStatus(
            @RequestBody UpdateOrderStatusRequest request) {

        return orderService.updateOrderStatus(request);
    }
    
 // INTERNAL ORDER STATUS UPDATE

    @PutMapping("/internal/status")
    public ResponseEntity<ApiResponse<UpdateOrderStatusResponse>> internalUpdateOrderStatus(
            @RequestBody UpdateOrderStatusRequest request) {

        return orderService.updateOrderStatus(request);
    }
}










