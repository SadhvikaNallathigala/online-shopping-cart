package com.example.shoppingcart.model;

import com.example.shoppingcart.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    private UUID productId;

    private String productName;

    private String category;

    private String brandName;

    private String modelName;

    private String color;

    private double price;

    private int quantity;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}