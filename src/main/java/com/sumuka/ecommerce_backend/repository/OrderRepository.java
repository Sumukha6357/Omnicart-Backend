package com.sumuka.ecommerce_backend.repository;

import com.sumuka.ecommerce_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    long countByOrderDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDate BETWEEN :start AND :end")
    BigDecimal calculateTotalRevenueBetween(LocalDate start, LocalDate end);

    List<Order> findByUserId(UUID userId);

    @Query("SELECT o FROM Order o JOIN FETCH o.user u JOIN FETCH o.items i JOIN FETCH i.product")
    List<Order> findAllWithDetails();

}
