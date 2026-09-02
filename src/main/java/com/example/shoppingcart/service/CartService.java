package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.CartRequest;
import com.example.shoppingcart.enums.ProductStatus;
import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.CartItemRepository;
import com.example.shoppingcart.repository.CartRepository;
import com.example.shoppingcart.repository.ProductRepository;
import com.example.shoppingcart.response.ApiResponse;
import com.example.shoppingcart.response.MetaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    private static final ZoneId IST =
            ZoneId.of("Asia/Kolkata");

    public ApiResponse<CartItem> addToCart(
            UUID customerId,
            CartRequest request) {

        Product product = productRepository.findById(
                request.getProductId()
        ).orElseThrow(() ->
                new IllegalArgumentException("Product not found"));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Product is not available");
        }

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        if (product.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException(
                    "Product is out of stock");
        }

        Cart cart = cartRepository
                .findByCustomerId(customerId)
                .orElseGet(() -> createCart(customerId));

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getCartId(),
                        product.getProductId())
                .orElseGet(() -> createItem(cart, product));

        item.setQuantity(
                item.getQuantity() + request.getQuantity());

        product.setQuantity(
                product.getQuantity() - request.getQuantity());

        product.setUpdatedAt(LocalDateTime.now(IST));

        productRepository.save(product);

        return new ApiResponse<>(
                true,
                cartItemRepository.save(item),
                null,
                new MetaResponse(
                        LocalDateTime.now(IST),
                        "Product added to cart successfully"));
    }

    private Cart createCart(UUID customerId) {

        Cart cart = new Cart();
        cart.setCustomerId(customerId);

        LocalDateTime now = LocalDateTime.now(IST);
        cart.setCreatedAt(now);
        cart.setUpdatedAt(now);

        return cartRepository.save(cart);
    }

    private CartItem createItem(
            Cart cart,
            Product product) {

        CartItem item = new CartItem();

        item.setCartId(cart.getCartId());
        item.setProductId(product.getProductId());
        item.setQuantity(0);

        return item;
    }

    public ApiResponse<CartItem> removeFromCart(
            UUID customerId,
            CartRequest request) {

        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Cart is empty"));

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getCartId(),
                        request.getProductId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found in cart"));

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        if (request.getQuantity() > item.getQuantity()) {
            throw new IllegalArgumentException(
                    "Remove quantity exceeds cart quantity");
        }

        Product product = productRepository.findById(
                request.getProductId()
        ).orElseThrow(() ->
                new IllegalArgumentException("Product not found"));

        item.setQuantity(
                item.getQuantity() - request.getQuantity());

        product.setQuantity(
                product.getQuantity() + request.getQuantity());

        product.setUpdatedAt(LocalDateTime.now(IST));

        productRepository.save(product);

        if (item.getQuantity() == 0) {
            cartItemRepository.delete(item);

            return new ApiResponse<>(
                    true,
                    null,
                    null,
                    new MetaResponse(
                            LocalDateTime.now(IST),
                            "Product removed from cart successfully"));
        }

        return new ApiResponse<>(
                true,
                cartItemRepository.save(item),
                null,
                new MetaResponse(
                        LocalDateTime.now(IST),
                        "Cart quantity updated successfully"));
    }

}