package com.example.shoppingcart.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    private UUID customerId;

    private double subtotal;

    private double discount;

    private double finalAmount;

    private String status;

    private LocalDateTime createdAt;
}