package com.example.shoppingcart.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@Data
public class Coupon {

    @Id
    @GeneratedValue
    private UUID couponId;

    private String code;

    private double discountPercentage;

    private boolean active;

    private LocalDateTime expiryDate;
}