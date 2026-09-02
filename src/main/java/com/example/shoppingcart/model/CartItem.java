package com.example.shoppingcart.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue
    private UUID cartItemId;

    private UUID cartId;
    private UUID productId;

    private int quantity;
}