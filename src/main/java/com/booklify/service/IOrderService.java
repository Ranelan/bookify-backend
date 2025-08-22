package com.booklify.service;

import com.booklify.domain.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService extends IService<Order, Long>{

    List<Order> getAll();
    List<Order> findByRegularUserId(Long id);
    List<Order> findByOrderDate(LocalDateTime orderDate);
}
