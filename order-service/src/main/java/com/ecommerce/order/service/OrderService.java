package com.ecommerce.order.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ecommerce.order.dto.CancelOrderRequest;
import com.ecommerce.order.dto.CancelOrderResponse;
import com.ecommerce.order.dto.GetAllOrdersRequest;
import com.ecommerce.order.dto.GetAllOrdersResponse;
import com.ecommerce.order.dto.GetMyOrdersRequest;
import com.ecommerce.order.dto.GetMyOrdersResponse;
import com.ecommerce.order.dto.GetOrderByIdRequest;
import com.ecommerce.order.dto.GetOrderByIdResponse;
import com.ecommerce.order.dto.PlaceOrderRequest;
import com.ecommerce.order.dto.PlaceOrderResponse;
import com.ecommerce.order.dto.UpdateOrderStatusRequest;
import com.ecommerce.order.dto.UpdateOrderStatusResponse;
import com.ecommerce.order.response.ApiResponse;

public interface OrderService {

   
    //  PLACE ORDER
   
    ResponseEntity<ApiResponse<PlaceOrderResponse>> placeOrder(
            PlaceOrderRequest request
    );

    //  GET ORDER BY ID
    
    ResponseEntity<ApiResponse<GetOrderByIdResponse>> getOrderById(
            GetOrderByIdRequest request
    );

    
    //  GET MY ORDERS
    
    ResponseEntity<ApiResponse<List<GetMyOrdersResponse>>> getMyOrders(
            GetMyOrdersRequest request
    );

    
    //  CANCEL ORDER
    
    ResponseEntity<ApiResponse<CancelOrderResponse>> cancelOrder(
            CancelOrderRequest request
    );

   
    //  UPDATE ORDER STATUS (ADMIN)
   
    ResponseEntity<ApiResponse<UpdateOrderStatusResponse>> updateOrderStatus(
            UpdateOrderStatusRequest request
    );

  
    //  GET ALL ORDERS (ADMIN)
   
    ResponseEntity<ApiResponse<List<GetAllOrdersResponse>>> getAllOrders(
            GetAllOrdersRequest request
    );
}