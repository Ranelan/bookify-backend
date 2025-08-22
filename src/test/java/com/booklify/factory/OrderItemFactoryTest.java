package com.booklify.factory;

import com.booklify.domain.Book;
import com.booklify.domain.Order;
import com.booklify.domain.OrderItem;
import com.booklify.domain.RegularUser;
import com.booklify.domain.enums.BookCondition;
import com.booklify.domain.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemFactoryTest {

    private int quantity;
    private double totalAmount;
    private OrderStatus orderStatus;
    private Order order;
    private Book book;
    private byte[] image;
    private RegularUser regularUser;

    @BeforeEach
    void setUp() {  // Initialize the test data
        // Create a sample book

        image = new byte[]{1, 2, 3};
        // Create a valid RegularUser
        regularUser = new RegularUserFactory().createRegularUser(
                "Test User",
                "test@example.com",
                "password123!",
                LocalDateTime.now(),
                4.5,
                "Test bio",
                LocalDateTime.now()
        );
        // Pass the valid RegularUser to OrderFactory
        order = new OrderFactory().createOrder(LocalDateTime.now(), regularUser);
        book = BookFactory.createBook(
                "9783161484100",
                "Atomic Habits",
                "James Clear",
                "Penguin Random House",
                BookCondition.ACCEPTABLE,
                19.99,
                "Minor marks in pages but readable",
                image,
                regularUser
        );

        quantity = 2;
        totalAmount = 39.98; // Assuming totalAmount is quantity * book price
        orderStatus = OrderStatus.PENDING;

    }

    @Test
    void createOrderItemFactory() {
        // Valid OrderItem creation
        OrderItem orderItem = OrderItemFactory.createOrderItemFactory(quantity, totalAmount, book, order, orderStatus);
        assertNotNull(orderItem);
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(totalAmount, orderItem.getTotalAmount());
        assertEquals(book, orderItem.getBook());
        assertEquals(order, orderItem.getOrder());
        assertEquals(orderStatus, orderItem.getOrderStatus());

        // Invalid cases
        assertNull(OrderItemFactory.createOrderItemFactory(-1, totalAmount, book, order, orderStatus)); // Invalid quantity
        assertNull(OrderItemFactory.createOrderItemFactory(quantity, -1.0, book, order, orderStatus)); // Invalid total amount
        assertNull(OrderItemFactory.createOrderItemFactory(quantity, totalAmount, null, order, orderStatus)); // Null book
        assertNull(OrderItemFactory.createOrderItemFactory(quantity, totalAmount, book, null, orderStatus)); // Null order
        assertNull(OrderItemFactory.createOrderItemFactory(quantity, totalAmount, book, order, null)); // Null orderStatus

    }

    @Test
    void createOrderItemFactoryWithInvalidData() {
        // Test with invalid quantity
        OrderItem invalidOrderItem = OrderItemFactory.createOrderItemFactory(-5, totalAmount, book, order, orderStatus);
        assertNull(invalidOrderItem);

        // Test with invalid total amount
        invalidOrderItem = OrderItemFactory.createOrderItemFactory(quantity, -10.0, book, order, orderStatus);
        assertNull(invalidOrderItem);

        // Test with null book
        invalidOrderItem = OrderItemFactory.createOrderItemFactory(quantity, totalAmount, null, order, orderStatus);
        assertNull(invalidOrderItem);

        // Test with null order
        invalidOrderItem = OrderItemFactory.createOrderItemFactory(quantity, totalAmount, book, null, orderStatus);
        assertNull(invalidOrderItem);
    }

    @Test
    void createOrderItemFactoryWithZeroQuantity() {
        // Test with zero quantity
        OrderItem invalidOrderItem = OrderItemFactory.createOrderItemFactory(0, totalAmount, book, order, orderStatus);
        assertNull(invalidOrderItem);
    }

    @Test
    void createOrderItemFactoryWithZeroTotalAmount() {
        // Test with zero total amount
        OrderItem invalidOrderItem = OrderItemFactory.createOrderItemFactory(quantity, 0.0, book, order, orderStatus);
        assertNull(invalidOrderItem);
    }
}
