package com.example.shoppingcart.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ApplyCouponRequest {

    private UUID couponId;

    private String code;
}