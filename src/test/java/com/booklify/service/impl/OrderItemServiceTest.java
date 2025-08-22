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

import java.time.LocalDateTime;
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

    @BeforeEach
    void setUp() {
        // Create and save a regular user
        regularUser = new RegularUser.RegularUserBuilder()
                .setFullName("Test User")
                .setEmail("testuser" + UUID.randomUUID() + "@example.com")
                .setPassword("password")
                .build();
        regularUser = regularUserRepository.save(regularUser);
        assertNotNull(regularUser.getId(), "Regular User ID should not be null after saving");

        // Create and save a book for the order item
        book = new Book.Builder()
                .setTitle("Test Book")
                .setAuthor("Test Author")
                .setPrice(19.99)
                .setCondition(BookCondition.EXCELLENT)
                .setUploadedDate(LocalDateTime.now())
                .setUser(regularUser)
                .build();
        book = bookRepository.save(book);
        assertNotNull(book.getBookID(), "Book ID should not be null after saving");

        // Create and save an order for the order item
        order = new Order.OrderBuilder()
                .setOrderDate(LocalDateTime.now())
                .setRegularUser(regularUser)
                .build();
        order = orderRepository.save(order);
        assertNotNull(order.getOrderId(), "Order ID should not be null after saving");

        // Create and save an order item
        orderItem = new OrderItem.OrderItemBuilder()
                .setOrder(order)
                .setBook(book)
                .setQuantity(2)
                .setOrderStatus(OrderStatus.PENDING)
                .build();
        orderItem = orderItemService.save(orderItem);
        assertNotNull(orderItem.getOrderItemId(), "Order Item ID should not be null after saving");
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void create() {
        // Create and save a new order item
        OrderItem newOrderItem = new OrderItem.OrderItemBuilder()
                .setOrder(order)
                .setBook(book)
                .setQuantity(3)
                .setOrderStatus(OrderStatus.PENDING)
                .build();
        OrderItem savedOrderItem = orderItemService.save(newOrderItem);
        assertNotNull(savedOrderItem, "Saved Order Item should not be null");
        assertNotNull(savedOrderItem.getOrderItemId(), "Saved Order Item ID should not be null");
        assertEquals(3, savedOrderItem.getQuantity(), "Saved Order Item quantity should match");
        assertEquals(book.getBookID(), savedOrderItem.getBook().getBookID(), "Saved Order Item book ID should match");
        assertEquals(order.getOrderId(), savedOrderItem.getOrder().getOrderId(), "Saved Order Item order ID should match");
        assertEquals(OrderStatus.PENDING, savedOrderItem.getOrderStatus(), "Saved Order Item status should match");
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void findById() {
        // Ensure the order item is saved before finding it
        assertNotNull(orderItem, "Order Item should not be null before finding by ID");

        // Find the order item by ID
        OrderItem foundOrderItem = orderItemService.findById(orderItem.getOrderItemId());
        assertNotNull(foundOrderItem, "Found Order Item should not be null");
        assertEquals(orderItem.getOrderItemId(), foundOrderItem.getOrderItemId(), "Found Order Item ID should match the original Order Item ID");
        assertEquals(orderItem.getQuantity(), foundOrderItem.getQuantity(), "Found Order Item quantity should match the original Order Item quantity");
        assertEquals(orderItem.getBook().getBookID(), foundOrderItem.getBook().getBookID(), "Found Order Item book ID should match the original Book ID");
        assertEquals(orderItem.getOrder().getOrderId(), foundOrderItem.getOrder().getOrderId(), "Found Order Item order ID should match the original Order ID");
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void update() {
        // Ensure the order item is saved before updating it
        assertNotNull(orderItem, "Order Item should not be null before updating");

        // Update the order item's quantity
        OrderItem updatedOrderItem = new OrderItem.OrderItemBuilder()
                .copy(orderItem)
                .setQuantity(5) // Update to a new quantity
                .build();

        // Update the order item using the service
        OrderItem savedUpdatedOrderItem = orderItemService.update(updatedOrderItem);
        assertNotNull(savedUpdatedOrderItem, "Updated Order Item should not be null");
        assertEquals(5, savedUpdatedOrderItem.getQuantity(), "Updated Order Item quantity should match the new quantity");
        assertEquals(orderItem.getOrderItemId(), savedUpdatedOrderItem.getOrderItemId(), "Updated Order Item ID should match the original Order Item ID");
        assertEquals(orderItem.getBook().getBookID(), savedUpdatedOrderItem.getBook().getBookID(), "Updated Order Item book ID should match the original Book ID");
        assertEquals(orderItem.getOrder().getOrderId(), savedUpdatedOrderItem.getOrder().getOrderId(), "Updated Order Item order ID should match the original Order ID");
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void findByOrderStatus() {
        // Ensure the order item is saved before finding by order status
        assertNotNull(orderItem, "Order Item should not be null before finding by order status");

        // Find order items by order status
        var foundOrderItems = orderItemService.findByOrderStatus(orderItem.getOrderStatus());
        assertFalse(foundOrderItems.isEmpty(), "Found Order Items should not be empty");
        assertTrue(foundOrderItems.stream().anyMatch(item -> item.getOrderItemId().equals(orderItem.getOrderItemId())),
                "Found Order Items should contain the original Order Item");
        assertEquals(orderItem.getOrderStatus(), foundOrderItems.get(0).getOrderStatus(), "Found Order Item status should match the original Order Item status");
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void findByOrderId() {
        // Ensure the order item is saved before finding by order ID
        assertNotNull(orderItem, "Order Item should not be null before finding by order ID");

        // Find order items by order ID
        var foundOrderItems = orderItemService.findByOrderId(order.getOrderId());
        assertFalse(foundOrderItems.isEmpty(), "Found Order Items should not be empty");
        assertTrue(foundOrderItems.stream().anyMatch(item -> item.getOrderItemId().equals(orderItem.getOrderItemId())),
                "Found Order Items should contain the original Order Item");
        assertEquals(order.getOrderId(), foundOrderItems.get(0).getOrder().getOrderId(), "Found Order Item order ID should match the original Order ID");
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void findByBookId() {
        // Ensure the order item is saved before finding by book ID
        assertNotNull(orderItem, "Order Item should not be null before finding by book ID");

        // Find order items by book ID
        var foundOrderItems = orderItemService.findByBookId(book.getBookID());
        assertFalse(foundOrderItems.isEmpty(), "Found Order Items should not be empty");
        assertTrue(foundOrderItems.stream().anyMatch(item -> item.getOrderItemId().equals(orderItem.getOrderItemId())),
                "Found Order Items should contain the original Order Item");
        assertEquals(book.getBookID(), foundOrderItems.get(0).getBook().getBookID(), "Found Order Item book ID should match the original Book ID");
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    void findByRegularUserId() {
        // Ensure the order item is saved before finding by regular user ID
        assertNotNull(orderItem, "Order Item should not be null before finding by regular user ID");

        // Find order items by regular user ID
        var foundOrderItems = orderItemService.findByRegularUserId(order.getRegularUser().getId());
        assertFalse(foundOrderItems.isEmpty(), "Found Order Items should not be empty");
        assertTrue(foundOrderItems.stream().anyMatch(item -> item.getOrderItemId().equals(orderItem.getOrderItemId())),
                "Found Order Items should contain the original Order Item");
        assertEquals(order.getRegularUser().getId(), foundOrderItems.get(0).getOrder().getRegularUser().getId(),
                "Found Order Item regular user ID should match the original Regular User ID");
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    void findAll() {
        // Ensure the order item is saved before finding all
        assertNotNull(orderItem, "Order Item should not be null before finding all");

        // Find all order items
        var foundOrderItems = orderItemService.findAll();
        assertFalse(foundOrderItems.isEmpty(), "Found Order Items should not be empty");
        assertTrue(foundOrderItems.stream().anyMatch(item -> item.getOrderItemId().equals(orderItem.getOrderItemId())),
                "Found Order Items should contain the original Order Item");
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    void deleteById() {
        // Ensure the order item is saved before deleting it
        assertNotNull(orderItem, "Order Item should not be null before deleting by ID");

        // Delete the order item by ID
        boolean isDeleted = orderItemService.deleteById(orderItem.getOrderItemId());
        assertTrue(isDeleted, "Order Item should be deleted successfully");

        // Verify that the order item no longer exists
        assertThrows(RuntimeException.class, () -> orderItemService.findById(orderItem.getOrderItemId()),
                "Finding deleted Order Item by ID should throw an exception");
    }
}
