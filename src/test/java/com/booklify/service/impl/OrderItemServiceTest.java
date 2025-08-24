package com.booklify.service.impl;

import com.booklify.domain.Book;
import com.booklify.domain.Order;
import com.booklify.domain.OrderItem;
import com.booklify.domain.RegularUser;
import com.booklify.domain.enums.BookCondition;
import com.booklify.domain.enums.OrderStatus;
import com.booklify.repository.BookRepository;
import com.booklify.repository.OrderRepository;
import com.booklify.repository.RegularUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional; // <-- IMPORT THIS

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderItemServiceTest {

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RegularUserRepository regularUserRepository;

    private OrderItem orderItem;
    private Order order;
    private Book book;
    private RegularUser regularUser;
    private static final double BOOK_PRICE = 25.50;

    @BeforeAll
    void initialSetup() {
        regularUser = new RegularUser.RegularUserBuilder()
                .setFullName("Service Test User")
                .setEmail("service-user-" + UUID.randomUUID().toString() + "@example.com")
                .setPassword("password123")
                .build();
        regularUser = regularUserRepository.save(regularUser);

        book = new Book.Builder()
                .setTitle("Book for Service Test")
                .setAuthor("Author for Service Test")
                .setPrice(BOOK_PRICE)
                .setCondition(BookCondition.EXCELLENT)
                .setUser(regularUser)
                .setUploadedDate(LocalDateTime.now())
                .build();
        book = bookRepository.save(book);

        order = new Order.OrderBuilder()
                .setOrderDate(LocalDateTime.now())
                .setRegularUser(regularUser)
                .build();
        order = orderRepository.save(order);
    }

    @BeforeEach
    void setUp() {
        orderItem = new OrderItem.OrderItemBuilder()
                .setOrder(order)
                .setBook(book)
                .setQuantity(2)
                .setOrderStatus(OrderStatus.PENDING)
                .build();
        orderItem = orderItemService.save(orderItem);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void save() {
        int quantity = 4;
        OrderItem newOrderItem = new OrderItem.OrderItemBuilder()
                .setOrder(order)
                .setBook(book)
                .setQuantity(quantity)
                .setOrderStatus(OrderStatus.PROCESSING)
                .build();
        OrderItem savedOrderItem = orderItemService.save(newOrderItem);
        assertNotNull(savedOrderItem);
        double expectedTotal = quantity * BOOK_PRICE;
        assertEquals(expectedTotal, savedOrderItem.getTotalAmount());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @Transactional // Good practice for finders to allow lazy loading in assertions
    void findById() {
        OrderItem foundOrderItem = orderItemService.findById(orderItem.getOrderItemId());
        assertNotNull(foundOrderItem);
        assertEquals(orderItem.getOrderItemId(), foundOrderItem.getOrderItemId());
        assertEquals(2 * BOOK_PRICE, foundOrderItem.getTotalAmount());
        // This access is now safe because of @Transactional
        assertEquals(order.getOrderId(), foundOrderItem.getOrder().getOrderId());
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void update() {
        int newQuantity = 10;
        OrderItem updateRequest = new OrderItem();
        updateRequest.setOrderItemId(orderItem.getOrderItemId());
        updateRequest.setQuantity(newQuantity);
        updateRequest.setOrderStatus(OrderStatus.SHIPPED);
        OrderItem result = orderItemService.update(updateRequest);
        assertNotNull(result);
        assertEquals(newQuantity, result.getQuantity());
        double expectedNewTotal = newQuantity * BOOK_PRICE;
        assertEquals(expectedNewTotal, result.getTotalAmount());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void findByOrderStatus() {
        List<OrderItem> results = orderItemService.findByOrderStatus(OrderStatus.PENDING);
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(item -> item.getOrderItemId().equals(orderItem.getOrderItemId())));
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    @Transactional // <-- FIX: Keeps the session open for lazy loading
    void findByOrderId() {
        List<OrderItem> results = orderItemService.findByOrderId(order.getOrderId());
        assertFalse(results.isEmpty());
        // This access to `.getOrder()` is now safe
        assertEquals(order.getOrderId(), results.get(0).getOrder().getOrderId());
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    @Transactional // <-- FIX: Keeps the session open for lazy loading
    void findByBookId() {
        List<OrderItem> results = orderItemService.findByBookId(book.getBookID());
        assertFalse(results.isEmpty());
        // This access to `.getBook()` is now safe
        assertEquals(book.getBookID(), results.get(0).getBook().getBookID());
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    @Transactional // <-- FIX: Keeps the session open for lazy loading
    void findByRegularUserId() {
        List<OrderItem> results = orderItemService.findByRegularUserId(regularUser.getId());
        assertFalse(results.isEmpty());
        // This access chain is now safe because the session is open
        assertEquals(regularUser.getId(), results.get(0).getOrder().getRegularUser().getId());
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    void findAll() {
        List<OrderItem> foundOrderItems = orderItemService.findAll();
        assertFalse(foundOrderItems.isEmpty());
        assertTrue(foundOrderItems.stream().anyMatch(item -> item.getOrderItemId().equals(orderItem.getOrderItemId())));
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    void deleteById() {
        boolean isDeleted = orderItemService.deleteById(orderItem.getOrderItemId());
        assertTrue(isDeleted);
        assertThrows(RuntimeException.class, () -> orderItemService.findById(orderItem.getOrderItemId()));
    }
}