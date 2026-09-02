package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.OrderDetailsResponse;
import com.example.shoppingcart.dto.OrderItemResponse;
import com.example.shoppingcart.dto.CheckoutRequest;
import com.example.shoppingcart.dto.CheckoutResponse;
import com.example.shoppingcart.dto.CancelOrderResponse;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.Order;
import com.example.shoppingcart.model.OrderItem;

import com.example.shoppingcart.repository.CartItemRepository;
import com.example.shoppingcart.repository.CartRepository;
import com.example.shoppingcart.repository.ProductRepository;
import com.example.shoppingcart.repository.OrderRepository;
import com.example.shoppingcart.repository.OrderItemRepository;

import com.example.shoppingcart.response.ApiResponse;
import com.example.shoppingcart.response.MetaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shoppingcart.dto.ApplyCouponRequest;
import com.example.shoppingcart.entity.Coupon;
import com.example.shoppingcart.repository.CouponRepository;

import com.example.shoppingcart.entity.Customer;
import com.example.shoppingcart.repository.CustomerRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CouponRepository couponRepository;


    public ApiResponse<OrderDetailsResponse> getOrderDetails(
            UUID customerId) {

        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Cart not found"));

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getCartId());

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        List<OrderItemResponse> items = new ArrayList<>();

        double subtotal = 0;

        for (CartItem cartItem : cartItems) {

            Product product = productRepository
                    .findById(cartItem.getProductId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Product not found"));

            double total =
                    product.getPrice() * cartItem.getQuantity();

            OrderItemResponse item =
                    new OrderItemResponse();

            item.setProductName(
                    product.getProductName());

            item.setBrandName(
                    product.getBrandName());

            item.setQuantity(
                    cartItem.getQuantity());

            item.setPrice(
                    product.getPrice());

            item.setTotal(total);

            items.add(item);

            subtotal += total;
        }

        OrderDetailsResponse response =
                new OrderDetailsResponse();

        response.setItems(items);
        response.setSubtotal(subtotal);
        response.setDiscount(0);
        response.setFinalAmount(subtotal);

        return new ApiResponse<>(
                true,
                response,
                null,
                new MetaResponse(
                        LocalDateTime.now(),
                        "Order details retrieved successfully"));
    }


    @Transactional
    public ApiResponse<CheckoutResponse> checkout(
            UUID customerId,
            CheckoutRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Checkout details are required");
        }

        if (!request.isConfirm()) {
            throw new IllegalArgumentException(
                    "Checkout confirmation is required");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found"));

        if (customer.isDeleted()) {
            throw new IllegalArgumentException(
                    "Customer not found");
        }

        Cart cart = cartRepository
                .findByCustomerId(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cart not found"));

        List<CartItem> cartItems =
                cartItemRepository
                        .findByCartId(cart.getCartId());

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cart is empty");
        }

        double subtotal = 0;

        for (CartItem cartItem : cartItems) {

            Product product = productRepository
                    .findById(cartItem.getProductId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Product not found"));

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient product quantity");
            }

            double total =
                    product.getPrice()
                            * cartItem.getQuantity();

            subtotal += total;
        }

        /*
         * Apply coupon if provided.
         */
        double discount = 0;
        String couponCode = null;

        if (request.getCouponId() != null
                || (request.getCouponCode() != null
                && !request.getCouponCode().isBlank())) {

            if (request.getCouponId() == null) {
                throw new IllegalArgumentException(
                        "Coupon ID is required");
            }

            if (request.getCouponCode() == null
                    || request.getCouponCode().isBlank()) {
                throw new IllegalArgumentException(
                        "Coupon code is required");
            }

            Coupon coupon = couponRepository
                    .findById(request.getCouponId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Invalid coupon"));

            if (!coupon.getCode()
                    .equalsIgnoreCase(
                            request.getCouponCode().trim())) {

                throw new IllegalArgumentException(
                        "Coupon ID and coupon code do not match");
            }

            if (!coupon.isActive()) {
                throw new IllegalArgumentException(
                        "Coupon is inactive");
            }

            if (coupon.getExpiryDate() == null) {
                throw new IllegalArgumentException(
                        "Coupon expiry date is required");
            }

            if (coupon.getExpiryDate()
                    .isBefore(LocalDateTime.now())) {

                throw new IllegalArgumentException(
                        "Coupon has expired");
            }

            if (coupon.getDiscountPercentage() <= 0
                    || coupon.getDiscountPercentage() > 100) {

                throw new IllegalArgumentException(
                        "Invalid discount percentage");
            }

            discount =
                    subtotal
                            * coupon.getDiscountPercentage()
                            / 100;

            couponCode = coupon.getCode();
        }

        double finalAmount = subtotal - discount;

        /*
         * Create Order.
         */
        Order order = new Order();

        order.setCustomerId(customerId);
        order.setSubtotal(subtotal);
        order.setDiscount(discount);
        order.setFinalAmount(finalAmount);
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder =
                orderRepository.save(order);

        /*
         * Create OrderItems.
         */
        for (CartItem cartItem : cartItems) {

            Product product = productRepository
                    .findById(cartItem.getProductId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Product not found"));

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrderId(
                    savedOrder.getOrderId());

            orderItem.setProductId(
                    product.getProductId());

            orderItem.setProductName(
                    product.getProductName());

            orderItem.setBrandName(
                    product.getBrandName());

            orderItem.setQuantity(
                    cartItem.getQuantity());

            orderItem.setPrice(
                    product.getPrice());

            double total =
                    product.getPrice()
                            * cartItem.getQuantity();

            orderItem.setTotal(total);

            orderItemRepository.save(orderItem);

            /*
             * Reduce product stock.
             */
            product.setQuantity(
                    product.getQuantity()
                            - cartItem.getQuantity());

            productRepository.save(product);
        }

        /*
         * Clear cart after successful checkout.
         */
        cartItemRepository.deleteAll(cartItems);

        /*
         * Prepare checkout response.
         */
        CheckoutResponse response =
                new CheckoutResponse();

        response.setOrderId(
                savedOrder.getOrderId());

        response.setCustomerId(
                customerId);

        response.setCustomerName(
                customer.getName());

        response.setEmail(
                customer.getEmail());

        response.setPhoneNumber(
                customer.getPhoneNumber());

        response.setSubtotal(
                subtotal);

        response.setDiscount(
                discount);

        response.setFinalAmount(
                finalAmount);

        response.setCouponCode(
                couponCode);

        response.setStatus(
                "PLACED");

        return new ApiResponse<>(
                true,
                response,
                null,
                new MetaResponse(
                        LocalDateTime.now(),
                        "Order placed successfully"));
    }


    public ApiResponse<OrderDetailsResponse> applyCoupon(
            UUID customerId,
            ApplyCouponRequest request) {

        OrderDetailsResponse response =
                getOrderDetails(customerId).getData();

        Coupon coupon = couponRepository
                .findByCode(request.getCode())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid coupon"));

        if (!coupon.isActive()) {
            throw new IllegalArgumentException(
                    "Coupon is inactive");
        }

        if (coupon.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Coupon has expired");
        }

        double discount =
                response.getSubtotal()
                        * coupon.getDiscountPercentage() / 100;

        response.setDiscount(discount);

        response.setFinalAmount(
                response.getSubtotal() - discount);

        response.setCouponCode(
                coupon.getCode());

        return new ApiResponse<>(
                true,
                response,
                null,
                new MetaResponse(
                        LocalDateTime.now(),
                        "Coupon applied successfully"));
    }


    /*
     * CANCEL ORDER
     */
    @Transactional
    public ApiResponse<CancelOrderResponse> cancelOrder(
            UUID customerId,
            UUID orderId) {

        if (customerId == null) {
            throw new IllegalArgumentException(
                    "Customer ID is required");
        }

        if (orderId == null) {
            throw new IllegalArgumentException(
                    "Order ID is required");
        }

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Order not found"));

        if (order.getCustomerId() == null
                || !order.getCustomerId().equals(customerId)) {

            throw new IllegalArgumentException(
                    "Order does not belong to this customer");
        }

        if (order.getStatus() == null) {
            throw new IllegalArgumentException(
                    "Order status is missing");
        }

        if (!order.getStatus().equalsIgnoreCase("PLACED")) {
            throw new IllegalArgumentException(
                    "Order cannot be cancelled because its current status is "
                            + order.getStatus());
        }

        order.setStatus("CANCELLED");

        Order savedOrder =
                orderRepository.save(order);

        CancelOrderResponse response =
                new CancelOrderResponse();

        response.setOrderId(
                savedOrder.getOrderId());

        response.setCustomerId(
                savedOrder.getCustomerId());

        response.setStatus(
                savedOrder.getStatus());

        response.setMessage(
                "Order cancelled successfully");

        return new ApiResponse<>(
                true,
                response,
                null,
                new MetaResponse(
                        LocalDateTime.now(),
                        "Order cancelled successfully"));
    }

    @Autowired
    private CustomerRepository customerRepository;
}