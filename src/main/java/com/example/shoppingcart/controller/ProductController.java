package com.example.shoppingcart.controller;

import com.example.shoppingcart.dto.ProductRequest;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.response.ApiResponse;
import com.example.shoppingcart.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ApiResponse<Product> addProduct(
            @RequestBody ProductRequest request) {

        return productService.addProduct(request);
    }



    @GetMapping
    public ApiResponse<Page<Product>> searchProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        return productService.searchProducts(
                search,
                category,
                brandName,
                color,
                minPrice,
                maxPrice,
                page,
                size);
    }
}