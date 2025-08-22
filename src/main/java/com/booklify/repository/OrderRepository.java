package com.booklify.repository;

import com.booklify.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRegularUserId(Long id);
    List<Order> findByOrderDate(LocalDateTime orderDate);

    @Query("SELECT o FROM Order o WHERE o.orderDate >= :startOfDay AND o.orderDate < :endOfDay")
    List<Order> findByOrderDateBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}

