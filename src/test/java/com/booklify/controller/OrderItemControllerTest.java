package com.booklify.controller;

import com.booklify.domain.Order;
import com.booklify.domain.OrderItem;
import com.booklify.domain.RegularUser;
import com.booklify.factory.OrderFactory;
import com.booklify.factory.RegularUserFactory;
import com.booklify.repository.BookRepository;
import com.booklify.repository.OrderItemRepository;
import com.booklify.repository.OrderRepository;
import com.booklify.repository.RegularUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderItemControllerTest {

    private Order order;
    private OrderItem orderItem;
    private RegularUser regularUser;
    private com.booklify.domain.Book book;

    @LocalServerPort
    private int port;

    @Autowired
    private RegularUserRepository regularUserRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private BookRepository bookRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/orderItems";
    }



    @BeforeAll
    void setUp() {
        // Use a random email to avoid unique constraint violation
        String randomEmail = "testUser_" + UUID.randomUUID() + "@gmail.com";
        regularUser = RegularUserFactory.createRegularUser("Test User", randomEmail, "password123", LocalDateTime.now(), 4.5, "Test bio", LocalDateTime.now());
        regularUser = regularUserRepository.save(regularUser);

        // Create and save a book for all tests
        book = new com.booklify.domain.Book.Builder()
                .setTitle("Default Test Book")
                .setAuthor("Default Author")
                .setCondition(com.booklify.domain.enums.BookCondition.EXCELLENT)
                .setPrice(10.0)
                .setDescription("Default Description")
                .setIsbn("0000000000000")
                .setPublisher("Default Publisher")
                .setUploadedDate(LocalDateTime.now())
                .setUser(regularUser)
                .build();
        book = bookRepository.save(book);

        // Create an order for the user using the builder's public setters
        order = new Order.OrderBuilder()
                .setOrderDate(LocalDateTime.now())
                .setRegularUser(regularUser)
                .build();
        order = orderRepository.save(order); // Save the order to get an ID
        orderItem = new OrderItem();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void createOrderItem() {
        orderItem = new OrderItem.OrderItemBuilder()
                .setOrder(order)
                .setBook(book)
                .setQuantity(2)
                .setOrderStatus(com.booklify.domain.enums.OrderStatus.PENDING)
                .build();

        OrderItem saved = orderItemRepository.save(orderItem);
        assertNotNull(saved.getOrderItemId(), "OrderItem ID should not be null after save");
        assertEquals(order.getOrderId(), saved.getOrder().getOrderId(), "OrderItem should be linked to the correct order");
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void updateOrderItem() {
        orderItem = new OrderItem.OrderItemBuilder()
                .setOrder(order)
                .setBook(book)
                .setQuantity(1)
                .setOrderStatus(com.booklify.domain.enums.OrderStatus.PENDING)
                .build();
        orderItem = orderItemRepository.save(orderItem);

        OrderItem updatedOrderItem = new OrderItem.OrderItemBuilder()
                .setOrderItemId(orderItem.getOrderItemId())
                .setOrder(order)
                .setBook(book)
                .setQuantity(5)
                .setOrderStatus(com.booklify.domain.enums.OrderStatus.PENDING)
                .build();
        OrderItem updated = orderItemRepository.save(updatedOrderItem);
        assertEquals(5, updated.getQuantity(), "OrderItem quantity should be updated");
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void deleteOrderItem() {
        OrderItem item = new OrderItem.OrderItemBuilder()
                .setOrder(order)
                .setBook(book)
                .setQuantity(3)
                .setOrderStatus(com.booklify.domain.enums.OrderStatus.PENDING)
                .build();
        item = orderItemRepository.save(item);
        Long id = item.getOrderItemId();
        assertNotNull(id);
        orderItemRepository.deleteById(id);
        assertFalse(orderItemRepository.findById(id).isPresent(), "OrderItem should be deleted");
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void getOrderItemById() {
        OrderItem item = new OrderItem.OrderItemBuilder()
                .setOrder(order)
                .setBook(book)
                .setQuantity(4)
                .setOrderStatus(com.booklify.domain.enums.OrderStatus.PENDING)
                .build();
        item = orderItemRepository.save(item);
        Long id = item.getOrderItemId();
        assertNotNull(id);
        OrderItem found = orderItemRepository.findById(id).orElse(null);
        assertNotNull(found, "OrderItem should be found by ID");
        assertEquals(4, found.getQuantity());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void findAllOrderItems() {
        long countBefore = orderItemRepository.count();
        OrderItem item = new OrderItem.OrderItemBuilder()
                .setOrder(order)
                .setBook(book)
                .setQuantity(6)
                .setOrderStatus(com.booklify.domain.enums.OrderStatus.PENDING)
                .build();
        orderItemRepository.save(item);
        Iterable<OrderItem> all = orderItemRepository.findAll();
        assertTrue(all.iterator().hasNext(), "Should find at least one order item");
        assertTrue(orderItemRepository.count() >= countBefore + 1);
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void findByOrderStatus() {
        // Create and save an order item with a specific status
        OrderItem item = new OrderItem.OrderItemBuilder()
                .setOrder(order)
                .setBook(book)
                .setQuantity(2)
                .setOrderStatus(com.booklify.domain.enums.OrderStatus.PENDING)
                .build();
        orderItemRepository.save(item);
        var found = orderItemRepository.findByOrderStatus(com.booklify.domain.enums.OrderStatus.PENDING);
        assertFalse(found.isEmpty(), "Should find order items by status");
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    void findByOrderId() {
        OrderItem item = new OrderItem.OrderItemBuilder()
                .setOrder(order)
                .setBook(book)
                .setQuantity(7)
                .setOrderStatus(com.booklify.domain.enums.OrderStatus.PENDING)
                .build();
        item = orderItemRepository.save(item);
        Long orderId = order.getOrderId();
        var found = orderItemRepository.findByOrder_OrderId(orderId);
        assertFalse(found.isEmpty(), "Should find order items by order ID");
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    void findByBookId() {
        com.booklify.domain.Book testBook = new com.booklify.domain.Book.Builder()
                .setTitle("Test Book")
                .setAuthor("Test Author")
                .setCondition(com.booklify.domain.enums.BookCondition.EXCELLENT)
                .setPrice(10.0)
                .setDescription("Test Description")
                .setIsbn("1234567890123")
                .setPublisher("Test Publisher")
                .setUploadedDate(LocalDateTime.now())
                .setUser(regularUser) // Ensure user is set
                .build();
        testBook = bookRepository.save(testBook);
        OrderItem item = new OrderItem.OrderItemBuilder()
                .setOrder(order)
                .setBook(testBook)
                .setQuantity(1)
                .setOrderStatus(com.booklify.domain.enums.OrderStatus.PENDING)
                .build();
        orderItemRepository.save(item);
        var found = orderItemRepository.findByBook_BookID(testBook.getBookID());
        assertFalse(found.isEmpty(), "Should find order items by book ID");
    }
}


