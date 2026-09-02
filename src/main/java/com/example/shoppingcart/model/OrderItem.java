package com.example.shoppingcart.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue
    private UUID orderItemId;

    private UUID orderId;

    private UUID productId;

    private String productName;

    private String brandName;

    private int quantity;

    private double price;

    private double total;
}