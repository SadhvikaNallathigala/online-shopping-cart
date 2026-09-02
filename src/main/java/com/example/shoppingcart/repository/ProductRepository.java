package com.example.shoppingcart.repository;

import com.example.shoppingcart.enums.ProductStatus;
import com.example.shoppingcart.model.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



import java.util.List;
import java.util.UUID;

public interface ProductRepository
        extends JpaRepository<Product, UUID> {

    List<Product> findByProductNameContainingIgnoreCaseAndStatus(
            String productName,
            ProductStatus status
    );

    List<Product> findByCategoryContainingIgnoreCaseAndStatus(
            String category,
            ProductStatus status
    );

    List<Product> findByBrandNameContainingIgnoreCaseAndStatus(
            String brandName,
            ProductStatus status
    );

    Page<Product> findByStatus(
            ProductStatus status,
            Pageable pageable
    );

    @Query("""
        SELECT p FROM Product p
        WHERE p.status = :status
        AND (:search IS NULL OR
             LOWER(p.productName) LIKE LOWER(CONCAT('%', :search, '%'))
             OR LOWER(p.category) LIKE LOWER(CONCAT('%', :search, '%'))
             OR LOWER(p.brandName) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:category IS NULL OR LOWER(p.category) = LOWER(:category))
        AND (:brandName IS NULL OR LOWER(p.brandName) = LOWER(:brandName))
        AND (:color IS NULL OR LOWER(p.color) = LOWER(:color))
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        """)
    Page<Product> filterProducts(
            @Param("search") String search,
            @Param("category") String category,
            @Param("brandName") String brandName,
            @Param("color") String color,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("status") ProductStatus status,
            Pageable pageable
    );
}