package com.booklify.service.impl;

import com.booklify.domain.Address;
import com.booklify.domain.Book;
import com.booklify.domain.Order;
import com.booklify.domain.OrderItem;
import com.booklify.dto.OrderCreateDto;
import com.booklify.domain.RegularUser;
import com.booklify.domain.enums.OrderStatus;
import com.booklify.repository.AddressRepository;
import com.booklify.repository.BookRepository;
import com.booklify.repository.OrderRepository;
import com.booklify.repository.RegularUserRepository;
import com.booklify.service.IOrderService;
import jakarta.transaction.Transactional;
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

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private BookRepository bookRepository;


    @Override
    @Transactional
    public Order save(Order order) {
        // 1. If a shipping address is provided, fetch the managed entity from the database.
        if (order.getShippingAddress() != null && order.getShippingAddress().getId() != null) {
            Address managedAddress = addressRepository.findById(order.getShippingAddress().getId())
                    .orElseThrow(() -> new RuntimeException("Address not found with id: " + order.getShippingAddress().getId()));
            order.setShippingAddress(managedAddress); // Requires a setter in the Order class
        }

        // 2. This is the critical part: Synchronize the children (OrderItems).
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                // a) Set the back-reference from the child (OrderItem) to the parent (Order).
                item.setOrder(order);

                // b) The Book from the client is a "detached" entity.
                //    Replace it with a "managed" entity from the database.
                if (item.getBook() != null && item.getBook().getBookID() != null) {
                    Book managedBook = bookRepository.findById(item.getBook().getBookID())
                            .orElseThrow(() -> new RuntimeException("Book not found with id: " + item.getBook().getBookID()));
                    item.setBook(managedBook);
                } else {
                    // It's good practice to validate that the item has a book.
                    throw new IllegalStateException("OrderItem must have a reference to a Book.");
                }
            }
        }

        // 3. Save the parent Order. Because of `CascadeType.ALL`, JPA will automatically
        //    save all the associated OrderItem entities in the same transaction.
        return orderRepository.save(order);
    }

    @Override
    public Order findById(Long aLong) {
        return orderRepository.findById(aLong)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + aLong));
    }

    @Override
    public Order update(Order entity) {
        Order existing = findById(entity.getOrderId());

        Address address = null;
        if (entity.getShippingAddress() != null && entity.getShippingAddress().getId() != null) {
            address = addressRepository.findById(entity.getShippingAddress().getId())
                    .orElseThrow(() -> new RuntimeException("Address not found with id: " + entity.getShippingAddress().getId()));
        }

        Order updatedOrder = new Order.OrderBuilder()
                .copy(existing)
                .setRegularUser(regularUserRepository.findById(entity.getRegularUser().getId())
                        .orElseThrow(() -> new RuntimeException("Regular User not found with id: " + entity.getRegularUser().getId())))
                .setOrderDate(entity.getOrderDate())
                .setShippingAddress(address)
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

    @Transactional
    @Override
    public Order createOrderFromDto(OrderCreateDto dto) {
        RegularUser user = regularUserRepository.findById(dto.getRegularUserId())
                .orElseThrow(() -> new RuntimeException("RegularUser not found with id: " + dto.getRegularUserId()));
        Address address = addressRepository.findById(dto.getShippingAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + dto.getShippingAddressId()));
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setRegularUser(user);
        order.setShippingAddress(address);
        for (OrderCreateDto.OrderItemCreateDto itemDto : dto.getOrderItems()) {
            Book book = bookRepository.findById(itemDto.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found with id: " + itemDto.getBookId()));
            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setQuantity(itemDto.getQuantity());
            item.setOrderStatus(OrderStatus.valueOf(itemDto.getOrderStatus()));
            item.setOrder(order);
            // totalAmount will be calculated by JPA callback
            order.getOrderItems().add(item);
        }
        Order savedOrder = orderRepository.save(order);

        // Decrease available books by quantity bought
        for (OrderItem item : savedOrder.getOrderItems()) {
            Book book = item.getBook();
            int newAvailable = book.getAvailable() - item.getQuantity();
            if (newAvailable < 0) {
                throw new IllegalStateException("Not enough books available for book id: " + book.getBookID());
            }
            book.setAvailable(newAvailable);
            // No need to set isAvailable manually; handled in setAvailable(int)
            bookRepository.save(book);
        }
        return savedOrder;
    }
}
