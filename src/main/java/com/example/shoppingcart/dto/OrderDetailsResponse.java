package com.example.shoppingcart.dto;

import lombok.Data;
import java.util.List;

import com.example.shoppingcart.dto.CheckoutRequest;
import com.example.shoppingcart.dto.CheckoutResponse;


@Data
public class OrderDetailsResponse {

    private List<OrderItemResponse> items;
    private double subtotal;
    private double discount;
    private double finalAmount;
    private String couponCode;
}