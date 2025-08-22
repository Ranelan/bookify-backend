package com.booklify.service.impl;


import com.booklify.domain.OrderItem;
import com.booklify.domain.enums.OrderStatus;
import com.booklify.repository.BookRepository;
import com.booklify.repository.OrderItemRepository;
import com.booklify.repository.OrderRepository;
import com.booklify.service.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderItemService implements IOrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;



    @Override
    public OrderItem save(OrderItem entity) {
        return orderItemRepository.save(entity);
    }

    @Override
    public OrderItem findById(Long aLong) {
        return orderItemRepository.findById(aLong)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + aLong));
    }

    @Override
    public OrderItem update(OrderItem entity) {
        OrderItem existing = findById(entity.getOrderItemId());

        OrderItem updatedOrderItem = new OrderItem.OrderItemBuilder()
                .copy(existing)
                .setBook(bookRepository.findById(entity.getBook().getBookID())
                        .orElseThrow(() -> new RuntimeException("Book not found with id: " + entity.getBook().getBookID())))
                .setOrder(orderRepository.findById(entity.getOrder().getOrderId())
                        .orElseThrow(() -> new RuntimeException("Order not found with id: " + entity.getOrder().getOrderId())))
                .setQuantity(entity.getQuantity())
                .setOrderStatus(entity.getOrderStatus() != null ? entity.getOrderStatus() : existing.getOrderStatus())
                .build();

        return orderItemRepository.save(updatedOrderItem);
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
        return orderItemRepository.findById(aLong)
                .map(orderItem -> {
                    orderItemRepository.delete(orderItem);
                    return true;
                })
                .orElse(false);
    }
}
