package com.booklify.service.impl;

import com.booklify.domain.OrderItem;
import com.booklify.domain.enums.OrderStatus;
import com.booklify.repository.OrderItemRepository;
import com.booklify.service.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import this

import java.util.List;

@Service
public class OrderItemService implements IOrderItemService {

    // Best practice: Use constructor injection with final fields
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem save(OrderItem entity) {
        // This is correct. The @PrePersist on the OrderItem entity handles
        // the initial totalAmount calculation before saving.
        return orderItemRepository.save(entity);
    }

    @Override
    public OrderItem findById(Long aLong) {
        return orderItemRepository.findById(aLong)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + aLong));
    }

    /**
     * Updates an existing OrderItem. This method only allows changing the quantity
     * and order status. The totalAmount is recalculated automatically.
     * The Book and parent Order associated with the item cannot be changed.
     */
    @Override
    @Transactional // Ensures the fetched entity remains managed by JPA
    public OrderItem update(OrderItem updatedInfo) {
        // 1. Fetch the existing, managed entity from the database.
        OrderItem existingOrderItem = findById(updatedInfo.getOrderItemId());

        // 2. Modify the properties of the existing entity directly.
        //    Do NOT build a new object.
        existingOrderItem.setQuantity(updatedInfo.getQuantity());

        if (updatedInfo.getOrderStatus() != null) {
            existingOrderItem.setOrderStatus(updatedInfo.getOrderStatus());
        }

        // --- Force initialization of the book field to avoid lazy loading issues ---
        if (existingOrderItem.getBook() == null) {
            throw new IllegalStateException("OrderItem's book must not be null during update.");
        }
        // Force initialization (triggers Hibernate to load the book if it's a proxy)
        existingOrderItem.getBook().getPrice();

        // 3. Save the modified entity.
        return orderItemRepository.save(existingOrderItem);
    }

    @Override
    public List<OrderItem> findByOrderStatus(OrderStatus orderStatus) {
        return orderItemRepository.findByOrderStatus(orderStatus);
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrder_OrderId(orderId);
    }

    @Override
    public List<OrderItem> findByBookId(Long bookId) {
        return orderItemRepository.findByBook_BookID(bookId);
    }

    @Override
    public List<OrderItem> findByRegularUserId(Long id) {
        return orderItemRepository.findByOrder_RegularUser_Id(id);
    }

    @Override
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public boolean deleteById(Long aLong) {
        if (orderItemRepository.existsById(aLong)) {
            orderItemRepository.deleteById(aLong);
            return true;
        }
        return false;
    }
}