package com.example.shoppingcart.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutResponse {

    private UUID orderId;
    private UUID customerId;

    private String customerName;
    private String email;
    private String phoneNumber;

    private double subtotal;
    private double discount;
    private double finalAmount;

    private String couponCode;

    private String status;
}