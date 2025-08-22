package com.booklify.factory;

import com.booklify.domain.Order;
import com.booklify.domain.RegularUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class OrderFactoryTest {

    private LocalDateTime orderDate;
    private RegularUser regularUser;

    @BeforeEach
    void setUp() {
        // Use a date in the past to avoid timing issues
        orderDate = LocalDateTime.now().minusSeconds(1);

        regularUser = new RegularUserFactory().createRegularUser("Engel Ranelani", "test@examole.com", "password123!",
                LocalDateTime.now(), 4.5, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                LocalDateTime.now());
    }

    @Test
    void createOrder() {
        Order order = OrderFactory.createOrder(orderDate, regularUser);
        assertNotNull(order);
        assertEquals(orderDate, order.getOrderDate());
        assertEquals(regularUser, order.getRegularUser());
    }

    @Test
    void createOrderWithNullRegularUser() {
        Order order = OrderFactory.createOrder(orderDate, null);
        assertNull(order);
    }

    @Test
    void createOrderWithInvalidDate() {
        LocalDateTime invalidDate = null; // Simulating an invalid date
        Order order = OrderFactory.createOrder(invalidDate, regularUser);
        assertNull(order);
    }
}

