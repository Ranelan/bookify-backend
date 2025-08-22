package com.booklify.service.impl;

import com.booklify.domain.Order;
import com.booklify.repository.OrderRepository;
import com.booklify.repository.RegularUserRepository;
import com.booklify.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RegularUserRepository regularUserRepository;


    @Override
    public Order save(Order entity) {
        return orderRepository.save(entity);
    }

    @Override
    public Order findById(Long aLong) {
        return orderRepository.findById(aLong)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + aLong));
    }

    @Override
    public Order update(Order entity) {
        Order existing = findById(entity.getOrderId());

        Order updatedOrder = new Order.OrderBuilder()
                .copy(existing)
                .setRegularUser(regularUserRepository.findById(entity.getRegularUser().getId())
                        .orElseThrow(() -> new RuntimeException("Regular User not found with id: " + entity.getRegularUser().getId())))
                .setOrderDate(entity.getOrderDate())
                .build();

        return orderRepository.save(updatedOrder);

    }

    @Override
    public boolean deleteById(Long aLong) {
        Order order = findById(aLong);
        orderRepository.delete(order);
        return true;
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findByRegularUserId(Long id) {
        return orderRepository.findByRegularUserId(id);
    }

    @Override
    public List<Order> findByOrderDate(LocalDateTime orderDate) {
        // Extract the date part and create start and end of day
        LocalDateTime startOfDay = orderDate.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        // Find orders between start and end of the day
        return orderRepository.findByOrderDateBetween(startOfDay, endOfDay);
    }
}
