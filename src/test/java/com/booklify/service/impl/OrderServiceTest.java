package com.booklify.service.impl;

import com.booklify.domain.Order;
import com.booklify.domain.RegularUser;
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
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RegularUserRepository regularUserRepository;

    private Order order;
    private RegularUser regularUser;

    @BeforeEach
    void setUp() {
        // Use a random email to avoid unique constraint violation
        String randomEmail = "testUser2_" + UUID.randomUUID() + "@gmail.com";
        regularUser = new RegularUser.RegularUserBuilder()
                .setFullName("Test User 2")
                .setEmail(randomEmail)
                .setPassword("password2")
                .setDateJoined(LocalDateTime.now())
                .build();
        regularUser = regularUserRepository.save(regularUser);
        assertNotNull(regularUser.getId(), "Regular User ID should not be null after saving");
        // Create and save an order for each test
        order = new Order.OrderBuilder()
                .setRegularUser(regularUser)
                .setOrderDate(LocalDateTime.now())
                .build();

    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void save() {
        // Create a new order with the initialized regular user
        order = new Order.OrderBuilder()
                .setRegularUser(regularUser)
                .setOrderDate(LocalDateTime.now())
                .build();

        // Save the order using the service
        Order savedOrder = orderService.save(order);

        // Validate that the saved order is not null and has an ID
        assertNotNull(savedOrder, "Saved order should not be null");
        assertNotNull(savedOrder.getOrderId(), "Saved order ID should not be null");
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void findById() {
        // Save the order first to ensure it has an ID
        Order savedOrder = orderService.save(order);
        assertNotNull(savedOrder.getOrderId(), "Order ID should not be null after saving");

        // Find the order by ID
        Order foundOrder = orderService.findById(savedOrder.getOrderId());

        // Validate that the found order matches the original order
        assertNotNull(foundOrder, "Found order should not be null");
        assertEquals(savedOrder.getOrderId(), foundOrder.getOrderId(), "Found order ID should match the saved order ID");
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void update() {
        // Save the order first to ensure it has an ID
        Order savedOrder = orderService.save(order);
        assertNotNull(savedOrder.getOrderId(), "Order ID should not be null after saving");

        // Update the order's order date
        Order updatedOrder = new Order.OrderBuilder()
                .copy(savedOrder)
                .setOrderDate(LocalDateTime.now().plusDays(1)) // Update to a new date
                .build();

        // Update the order using the service
        Order savedUpdatedOrder = orderService.update(updatedOrder);

        // Validate that the updated order is not null and has the new order date
        assertNotNull(savedUpdatedOrder, "Updated order should not be null");
        assertEquals(updatedOrder.getOrderDate(), savedUpdatedOrder.getOrderDate(),
                "Updated order date should match the new date");
        assertEquals(savedOrder.getOrderId(), savedUpdatedOrder.getOrderId(),
                "Updated order ID should match the original order ID");
        assertEquals(savedOrder.getRegularUser().getId(), savedUpdatedOrder.getRegularUser().getId(),
                "Updated order's regular user ID should match the original regular user ID");
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void getAll() {
        // Save the order first to ensure it is persisted
        Order savedOrder = orderService.save(order);
        assertNotNull(savedOrder.getOrderId(), "Order ID should not be null after saving");

        // Retrieve all orders
        var orders = orderService.getAll();

        // Validate that the list of orders is not null and contains the saved order
        assertNotNull(orders, "Orders list should not be null");
        assertFalse(orders.isEmpty(), "Orders list should not be empty");
        assertTrue(orders.stream().anyMatch(o -> o.getOrderId().equals(savedOrder.getOrderId())),
                "Orders list should contain the saved order");
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void findByRegularUserId() {
        // Save the order first to ensure it is persisted
        Order savedOrder = orderService.save(order);
        assertNotNull(savedOrder.getOrderId(), "Order ID should not be null after saving");

        // Retrieve orders by regular user ID
        var orders = orderService.findByRegularUserId(regularUser.getId());

        // Validate that the list of orders is not null and contains the saved order
        assertNotNull(orders, "Orders list should not be null");
        assertFalse(orders.isEmpty(), "Orders list should not be empty");
        assertTrue(orders.stream().anyMatch(o -> o.getOrderId().equals(savedOrder.getOrderId())),
                "Orders list should contain the saved order for the regular user");
    }



    @Test
    @org.junit.jupiter.api.Order(6)
    void findByOrderDate() {
        // Save the order first to ensure it is persisted
        Order savedOrder = orderService.save(order);
        assertNotNull(savedOrder.getOrderId(), "Order ID should not be null after saving");

        // Retrieve orders by order date
        var orders = orderService.findByOrderDate(savedOrder.getOrderDate());

        // Validate that the list of orders is not null and contains the saved order
        assertNotNull(orders, "Orders list should not be null");
        assertFalse(orders.isEmpty(), "Orders list should not be empty");
        assertTrue(orders.stream().anyMatch(o -> o.getOrderId().equals(savedOrder.getOrderId())),
                "Orders list should contain the saved order for the given order date");
    }
}