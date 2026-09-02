package com.example.shoppingcart.dto;

import lombok.Data;

@Data
public class ProductRequest {

    private String productName;

    private String category;

    private String brandName;

    private String modelName;

    private String color;

    private double price;

    private int quantity;

    private String description;
}