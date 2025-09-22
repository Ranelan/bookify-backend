package com.booklify.service;

import com.booklify.domain.Order;
import com.booklify.dto.OrderCreateDto;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService extends IService<Order, Long>{

    List<Order> getAll();
    List<Order> findByRegularUserId(Long id);
    List<Order> findByOrderDate(LocalDateTime orderDate);

    @Transactional
    Order createOrderFromDto(OrderCreateDto dto);
}
