package com.example.shoppingcart.controller;

import com.example.shoppingcart.dto.CustomerAuthRequest;
import com.example.shoppingcart.dto.CustomerResponse;
import com.example.shoppingcart.response.ApiResponse;
import com.example.shoppingcart.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.shoppingcart.entity.CustomerHistory;
import org.springframework.data.domain.Page;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/auth")
    public ApiResponse<CustomerResponse> registerOrLogin(
            @Valid @RequestBody CustomerAuthRequest request) {

        return customerService.registerOrLogin(request);
    }

    @GetMapping("/{customerId}/history")
    public ApiResponse<Page<CustomerHistory>> getHistory(
            @PathVariable UUID customerId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        return customerService.getHistory(
                customerId, page, size);
    }
}