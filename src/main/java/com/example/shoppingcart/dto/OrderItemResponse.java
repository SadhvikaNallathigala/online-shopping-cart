package com.example.shoppingcart.dto;

import lombok.Data;

@Data
public class OrderItemResponse {

    private String productName;
    private String brandName;
    private int quantity;
    private double price;
    private double total;
}