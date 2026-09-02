package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.ProductRequest;
import com.example.shoppingcart.enums.ProductStatus;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.ProductRepository;
import com.example.shoppingcart.response.ApiResponse;
import com.example.shoppingcart.response.MetaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private static final ZoneId IST =
            ZoneId.of("Asia/Kolkata");


    // =========================================================
    // ADD PRODUCT
    // =========================================================

    public ApiResponse<Product> addProduct(ProductRequest request) {

        if (request.getPrice() <= 0) {
            throw new IllegalArgumentException(
                    "Product price must be greater than zero");
        }

        if (request.getQuantity() < 0) {
            throw new IllegalArgumentException(
                    "Product quantity cannot be negative");
        }

        List<Product> products =
                productRepository
                        .findByProductNameContainingIgnoreCaseAndStatus(
                                request.getProductName(),
                                ProductStatus.ACTIVE);

        for (Product product : products) {

            if (product.getBrandName().equalsIgnoreCase(
                    request.getBrandName())
                    && product.getModelName().equalsIgnoreCase(
                    request.getModelName())
                    && product.getColor().equalsIgnoreCase(
                    request.getColor())) {

                product.setQuantity(
                        product.getQuantity()
                                + request.getQuantity());

                product.setUpdatedAt(
                        LocalDateTime.now(IST));

                return response(
                        productRepository.save(product),
                        "Product quantity updated successfully");
            }
        }

        Product product = new Product();

        product.setProductName(
                request.getProductName());

        product.setCategory(
                request.getCategory());

        product.setBrandName(
                request.getBrandName());

        product.setModelName(
                request.getModelName());

        product.setColor(
                request.getColor());

        product.setPrice(
                request.getPrice());

        product.setQuantity(
                request.getQuantity());

        product.setDescription(
                request.getDescription());

        product.setStatus(
                ProductStatus.ACTIVE);

        LocalDateTime now =
                LocalDateTime.now(IST);

        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        return response(
                productRepository.save(product),
                "Product added successfully");
    }


    // =========================================================
    // GET / SEARCH / FILTER PRODUCTS
    // =========================================================

    public ApiResponse<Page<Product>> searchProducts(

            String search,
            String category,
            String brandName,
            String color,
            Double minPrice,
            Double maxPrice,
            Integer page,
            Integer size) {


        // -----------------------------------------------------
        // DEFAULT PAGINATION
        // -----------------------------------------------------

        if (page == null) {
            page = 0;
        }

        if (size == null) {
            size = 10;
        }


        // -----------------------------------------------------
        // PAGINATION VALIDATION
        // -----------------------------------------------------

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative");
        }

        if (size <= 0) {
            throw new IllegalArgumentException(
                    "Page size must be greater than zero");
        }


        // -----------------------------------------------------
        // PRICE VALIDATION
        // -----------------------------------------------------

        if (minPrice != null && minPrice < 0) {
            throw new IllegalArgumentException(
                    "Minimum price cannot be negative");
        }

        if (maxPrice != null && maxPrice < 0) {
            throw new IllegalArgumentException(
                    "Maximum price cannot be negative");
        }

        if (minPrice != null
                && maxPrice != null
                && minPrice > maxPrice) {

            throw new IllegalArgumentException(
                    "Minimum price cannot be greater than maximum price");
        }


        // -----------------------------------------------------
        // EMPTY STRING HANDLING
        // -----------------------------------------------------

        if (search != null
                && search.trim().isEmpty()) {

            search = null;
        }

        if (category != null
                && category.trim().isEmpty()) {

            category = null;
        }

        if (brandName != null
                && brandName.trim().isEmpty()) {

            brandName = null;
        }

        if (color != null
                && color.trim().isEmpty()) {

            color = null;
        }


        // -----------------------------------------------------
        // FILTER PRODUCTS
        // -----------------------------------------------------

        Page<Product> products =
                productRepository.filterProducts(

                        search,
                        category,
                        brandName,
                        color,
                        minPrice,
                        maxPrice,

                        ProductStatus.ACTIVE,

                        PageRequest.of(page, size)
                );


        // -----------------------------------------------------
        // RESPONSE
        // -----------------------------------------------------

        return new ApiResponse<>(

                true,

                products,

                null,

                new MetaResponse(
                        LocalDateTime.now(IST),
                        "Products retrieved successfully")
        );
    }


    // =========================================================
    // GET PRODUCT BY ID
    // =========================================================

    public Product getProductById(
            UUID productId) {

        Product product =
                productRepository.findById(productId)

                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Product not found"));


        if (product.getStatus()
                == ProductStatus.INACTIVE) {

            throw new IllegalArgumentException(
                    "Product not found");
        }

        return product;
    }


    // =========================================================
    // DECREASE PRODUCT QUANTITY
    // =========================================================

    public void decreaseQuantity(
            UUID productId,
            int quantity) {

        Product product =
                getProductById(productId);


        if (quantity <= 0) {

            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }


        if (product.getQuantity()
                < quantity) {

            throw new IllegalArgumentException(
                    "Product is out of stock");
        }


        product.setQuantity(
                product.getQuantity()
                        - quantity);

        product.setUpdatedAt(
                LocalDateTime.now(IST));


        productRepository.save(product);
    }


    // =========================================================
    // INCREASE PRODUCT QUANTITY
    // =========================================================

    public void increaseQuantity(
            UUID productId,
            int quantity) {

        Product product =
                getProductById(productId);


        if (quantity <= 0) {

            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }


        product.setQuantity(
                product.getQuantity()
                        + quantity);

        product.setUpdatedAt(
                LocalDateTime.now(IST));


        productRepository.save(product);
    }


    // =========================================================
    // COMMON PRODUCT RESPONSE
    // =========================================================

    private ApiResponse<Product> response(
            Product product,
            String message) {

        return new ApiResponse<>(

                true,

                product,

                null,

                new MetaResponse(
                        LocalDateTime.now(IST),
                        message)
        );
    }
}