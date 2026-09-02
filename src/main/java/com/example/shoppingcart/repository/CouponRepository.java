package com.example.shoppingcart.repository;

import com.example.shoppingcart.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository
        extends JpaRepository<Coupon, UUID> {

    Optional<Coupon> findByCode(String code);
}