package com.booklify.repository;

import com.booklify.domain.OrderItem;
import com.booklify.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository <OrderItem, Long>{

    List<OrderItem> findByOrderStatus(OrderStatus orderStatus);
    List<OrderItem> findByOrder_OrderId(Long orderId);
    List<OrderItem> findByBook_BookID(Long bookId);
    List<OrderItem> findByOrder_RegularUser_Id(Long id);
}

