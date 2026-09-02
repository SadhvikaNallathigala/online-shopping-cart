package com.example.shoppingcart.repository;

import com.example.shoppingcart.entity.CustomerHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerHistoryRepository
        extends JpaRepository<CustomerHistory, UUID> {

    Page<CustomerHistory> findByCustomerIdOrderByTimestampDesc(
            UUID customerId,
            Pageable pageable);
}