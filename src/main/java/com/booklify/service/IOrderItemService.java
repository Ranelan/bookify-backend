package com.booklify.service;

import com.booklify.domain.OrderItem;
import com.booklify.domain.enums.OrderStatus;

import java.util.List;

public interface IOrderItemService extends IService<OrderItem, Long>{

    List<OrderItem> findByOrderStatus(OrderStatus orderStatus);
    List<OrderItem> findByOrderId(Long orderId);
    List<OrderItem> findByBookId(Long bookId);
    List<OrderItem> findByRegularUserId(Long id);
    List<OrderItem> findAll();
}
