package com.booklify.repository;

import com.booklify.domain.OrderItem;
import com.booklify.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Keep your existing finder methods
    List<OrderItem> findByOrderStatus(OrderStatus orderStatus);
    List<OrderItem> findByOrder_OrderId(Long orderId);
    List<OrderItem> findByBook_BookID(Long bookId);
    List<OrderItem> findByOrder_RegularUser_Id(Long id);

    // --- UPDATED METHODS FOR REVENUE CALCULATION ---

    /**
     * Calculates the sum of totalAmount for ALL order items, regardless of status.
     * COALESCE ensures we get 0.0 instead of null if the table is empty.
     */
    @Query("SELECT COALESCE(SUM(oi.totalAmount), 0.0) FROM OrderItem oi")
    Double sumTotalAmountOfAllItems();

    /**
     * Calculates the sum of totalAmount for ALL order items within a specific date range,
     * regardless of status.
     */
    @Query("SELECT COALESCE(SUM(oi.totalAmount), 0.0) FROM OrderItem oi " +
            "WHERE oi.order.orderDate BETWEEN :startDate AND :endDate")
    Double sumTotalAmountByOrderDateBetween(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);
}