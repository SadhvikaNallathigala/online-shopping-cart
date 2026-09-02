package com.example.shoppingcart.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CartRequest {

    private UUID productId;
    private int quantity;
}