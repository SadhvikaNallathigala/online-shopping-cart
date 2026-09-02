package com.example.shoppingcart.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {

    private boolean confirm;

    private UUID couponId;

    private String couponCode;
}