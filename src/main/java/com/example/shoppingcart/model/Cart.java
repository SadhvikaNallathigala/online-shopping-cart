package com.example.shoppingcart.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue
    private UUID cartId;

    private UUID customerId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}