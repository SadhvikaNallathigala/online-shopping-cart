package com.example.shoppingcart.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CancelOrderResponse {

    private UUID orderId;
    private UUID customerId;
    private String status;
    private String message;
}