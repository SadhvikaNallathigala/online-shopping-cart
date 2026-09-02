package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.CouponRequest;
import com.example.shoppingcart.entity.Coupon;
import com.example.shoppingcart.exception.CustomExceptions;
import com.example.shoppingcart.repository.CouponRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    private static final ZoneId IST =
            ZoneId.of("Asia/Kolkata");

    public Coupon applyCoupon(CouponRequest request) {

        if (request == null) {
            throw new CustomExceptions.InvalidCouponException(
                    "Coupon details are required");
        }

        if (request.getCouponId() == null) {
            throw new CustomExceptions.InvalidCouponException(
                    "Coupon ID is required");
        }

        if (request.getCode() == null || request.getCode().isBlank()) {
            throw new CustomExceptions.InvalidCouponException(
                    "Coupon code is required");
        }

        Coupon coupon = couponRepository
                .findById(request.getCouponId())
                .orElseThrow(() ->
                        new CustomExceptions.InvalidCouponException(
                                "Invalid coupon"));

        /*
         * Check that the coupon ID and coupon code
         * belong to the same coupon.
         */
        if (!coupon.getCode()
                .equalsIgnoreCase(request.getCode().trim())) {

            throw new CustomExceptions.InvalidCouponException(
                    "Coupon ID and coupon code do not match");
        }

        if (!coupon.isActive()) {
            throw new CustomExceptions.InvalidCouponException(
                    "Coupon is inactive");
        }

        if (coupon.getExpiryDate() == null) {
            throw new CustomExceptions.InvalidCouponException(
                    "Coupon expiry date is required");
        }

        if (coupon.getExpiryDate()
                .isBefore(LocalDateTime.now(IST))) {

            throw new CustomExceptions.InvalidCouponException(
                    "Coupon has expired");
        }

        if (coupon.getDiscountPercentage() <= 0 ||
                coupon.getDiscountPercentage() > 100) {

            throw new CustomExceptions.InvalidCouponException(
                    "Invalid discount percentage");
        }

        return coupon;
    }
}