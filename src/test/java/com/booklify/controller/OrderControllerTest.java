package com.booklify.controller;

import com.booklify.domain.Order;
import com.booklify.domain.RegularUser;
import com.booklify.factory.OrderFactory;
import com.booklify.factory.RegularUserFactory;
import com.booklify.repository.RegularUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderControllerTest {

    private Order order;
    private RegularUser regularUser;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RegularUserRepository regularUserRepository;

    private String createdOrderDateStr;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/orders";
    }

    @BeforeAll
    void setUp() {

        // Use a random email to avoid unique constraint violation
        String randomEmail = "testUser_" + UUID.randomUUID() + "@gmail.com";
        regularUser = RegularUserFactory.createRegularUser("Test User", randomEmail, "password123", LocalDateTime.now(), 4.5, "Test bio", LocalDateTime.now());
        regularUser = regularUserRepository.save(regularUser);

        order = (Order) OrderFactory.createOrder(LocalDateTime.now(), regularUser);
        String url = getBaseUrl() + "/create";
        ResponseEntity<Order> response = testRestTemplate.postForEntity(url, order, Order.class);
        assertEquals(201, response.getStatusCode().value(), "Response status should be 201");
        assertNotNull(response.getBody(), "Response body should not be null");
        order = response.getBody(); // Use the created order with generated ID for all tests
        createdOrderDateStr = order.getOrderDate().withNano(0).toString(); // Store the original order date for date-based queries
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Order(1)
    void createOrder() {
        assertNotNull(order);
        assertNotNull(order.getOrderId(), "Order ID should not be null after creation");
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Order(2)
    void updateOrder() {
        String url = getBaseUrl() + "/update/" + order.getOrderId();
        // Use builder to update orderDate
        Order updatedOrder = new Order.OrderBuilder()
                .copy(order)
                .setOrderDate(order.getOrderDate().plusDays(1))
                .build();
        ResponseEntity<Order> response = testRestTemplate.exchange(url, org.springframework.http.HttpMethod.PUT, new org.springframework.http.HttpEntity<>(updatedOrder), Order.class);
        assertEquals(200, response.getStatusCode().value(), "Response status should be 200");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(order.getOrderId(), response.getBody().getOrderId(), "Order ID should match after update");
        assertEquals(updatedOrder.getOrderDate(), response.getBody().getOrderDate(), "Order date should be updated");
        // Update the stored order and date string for subsequent tests
        order = response.getBody();
        createdOrderDateStr = order.getOrderDate().withNano(0).toString();
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Order(3)
    void findByOrderById() {
        String url = getBaseUrl() + "/getById/" + order.getOrderId();
        ResponseEntity<Order> response = testRestTemplate.getForEntity(url, Order.class);
        assertEquals(200, response.getStatusCode().value(), "Response status should be 200");
        assertNotNull(response.getBody(), "Order should be found by ID");
        assertEquals(order.getOrderId(), response.getBody().getOrderId(), "Order ID should match");
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Order(4)
    void getAll() {
        String url = getBaseUrl() + "/getAll";
        ResponseEntity<Order[]> response = testRestTemplate.getForEntity(url, Order[].class);
        assertEquals(200, response.getStatusCode().value(), "Response status should be 200");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().length > 0, "There should be at least one order");
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Order(5)
    void getOrdersByUserId() {
        String url = getBaseUrl() + "/getByUserId/" + regularUser.getId();
        ResponseEntity<Order[]> response = testRestTemplate.getForEntity(url, Order[].class);
        assertEquals(200, response.getStatusCode().value(), "Response status should be 200");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().length > 0, "User should have at least one order");
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Order(6)
    void getOrdersByOrderDate() {
        String url = getBaseUrl() + "/getByOrderDate/" + createdOrderDateStr;
        System.out.println("Querying for orderDate: " + createdOrderDateStr);
        ResponseEntity<Order[]> response = testRestTemplate.getForEntity(url, Order[].class);
        assertEquals(200, response.getStatusCode().value(), "Response status should be 200");
        assertNotNull(response.getBody(), "Response body should not be null");
        System.out.println("Orders returned: " + response.getBody().length);
        for (Order o : response.getBody()) {
            System.out.println("OrderId: " + o.getOrderId() + ", orderDate: " + o.getOrderDate());
        }
        assertTrue(response.getBody().length > 0, "There should be at least one order for the given date");
    }


    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Order(7)
    void deleteOrder() {
        String url = getBaseUrl() + "/delete/" + order.getOrderId();
        ResponseEntity<Void> deleteResponse = testRestTemplate.exchange(url, org.springframework.http.HttpMethod.DELETE, null, Void.class);
        assertEquals(204, deleteResponse.getStatusCode().value(), "Delete should return 204 No Content");
        // Try to fetch the deleted order
        ResponseEntity<Order> response = testRestTemplate.getForEntity(getBaseUrl() + "/getById/" + order.getOrderId(), Order.class);
        assertEquals(404, response.getStatusCode().value(), "Order should not be found after deletion");
    }

}
