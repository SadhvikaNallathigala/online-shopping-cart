package com.example.shoppingcart.controller;

import com.example.shoppingcart.dto.CartRequest;
import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.response.ApiResponse;
import com.example.shoppingcart.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{customerId}/items")
    public ApiResponse<CartItem> addToCart(
            @PathVariable UUID customerId,
            @RequestBody CartRequest request) {

        return cartService.addToCart(customerId, request);
    }

    @DeleteMapping("/{customerId}/items")
    public ApiResponse<CartItem> removeFromCart(
            @PathVariable UUID customerId,
            @RequestBody CartRequest request) {

        return cartService.removeFromCart(
                customerId, request);
    }
}