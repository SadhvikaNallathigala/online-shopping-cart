package com.example.shoppingcart.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerResponse {

    private UUID customerId;

    private String name;

    private String email;

    private String phoneNumber;
}