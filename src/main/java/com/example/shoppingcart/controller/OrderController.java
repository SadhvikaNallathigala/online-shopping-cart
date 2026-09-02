package com.example.shoppingcart.controller;

import com.example.shoppingcart.dto.OrderDetailsResponse;
import com.example.shoppingcart.dto.CheckoutRequest;
import com.example.shoppingcart.dto.CheckoutResponse;
import com.example.shoppingcart.dto.CancelOrderResponse;
import com.example.shoppingcart.response.ApiResponse;
import com.example.shoppingcart.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.shoppingcart.dto.ApplyCouponRequest;
import com.example.shoppingcart.dto.OrderDetailsResponse;

import com.example.shoppingcart.dto.CancelOrderResponse;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/details/{customerId}")
    public ApiResponse<OrderDetailsResponse> getOrderDetails(
            @PathVariable UUID customerId) {

        return orderService.getOrderDetails(customerId);
    }

    @PostMapping("/checkout/{customerId}")
    public ApiResponse<CheckoutResponse> checkout(
            @PathVariable UUID customerId,
            @RequestBody CheckoutRequest request) {

        return orderService.checkout(customerId, request);
    }

    @PostMapping("/apply-coupon/{customerId}")
    public ApiResponse<OrderDetailsResponse> applyCoupon(
            @PathVariable UUID customerId,
            @RequestBody ApplyCouponRequest request) {

        return orderService.applyCoupon(customerId, request);
    }

    @PutMapping("/cancel/{customerId}/{orderId}")
    public ApiResponse<CancelOrderResponse> cancelOrder(
            @PathVariable UUID customerId,
            @PathVariable UUID orderId) {

        return orderService.cancelOrder(customerId, orderId);
    }
}