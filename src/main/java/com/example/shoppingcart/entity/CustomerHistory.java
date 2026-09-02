package com.example.shoppingcart.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "customer_history")
public class CustomerHistory {

    @Id
    @GeneratedValue
    private UUID historyId;

    private UUID customerId;

    private String action;

    private LocalDateTime timestamp;
}