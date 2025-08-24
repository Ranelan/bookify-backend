package com.booklify.factory;

import com.booklify.domain.Book;
import com.booklify.domain.Order;
import com.booklify.domain.OrderItem;
import com.booklify.domain.RegularUser;
import com.booklify.domain.enums.BookCondition;
import com.booklify.domain.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemFactoryTest {

    private Book book;
    private Order order;
    private RegularUser regularUser;
    private final int quantity = 2;
    private final double bookPrice = 19.99;

    @BeforeEach
    void setUp() {
        // --- Arrange ---
        // Create a valid RegularUser
        regularUser = new RegularUser.RegularUserBuilder() // Assuming RegularUser has a builder
                .setFullName("Test User")
                .setEmail("test@example.com")
                .setPassword("password123!")
                .build();

        // Create a valid Order
        order = new Order.OrderBuilder() // Assuming Order has a builder
                .setOrderDate(LocalDateTime.now())
                .setRegularUser(regularUser)
                .build();

        // Create a valid Book with a known price
        book = new Book.Builder() // Assuming Book has a builder
                .setIsbn("9783161484100")
                .setTitle("Atomic Habits")
                .setAuthor("James Clear")
                .setPrice(bookPrice) // Use the defined price
                .setCondition(BookCondition.ACCEPTABLE)
                .setUser(regularUser)
                .build();
    }

    @Test
    @DisplayName("Should create a valid OrderItem and calculate total amount")
    void shouldCreateValidOrderItem() {
        // --- Act ---
        // Call the updated factory method (no totalAmount is passed in)
        OrderItem orderItem = OrderItemFactory.createOrderItemFactory(quantity, book, order, OrderStatus.PENDING);

        // --- Assert ---
        assertNotNull(orderItem, "OrderItem should not be null for valid inputs.");

        // Assert that all properties are set correctly
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(book, orderItem.getBook());
        assertEquals(order, orderItem.getOrder());
        assertEquals(OrderStatus.PENDING, orderItem.getOrderStatus());

        // CRITICAL TEST: Assert that the total amount was calculated correctly
        // Expected total = quantity * book's price
        double expectedTotalAmount = quantity * bookPrice;
        assertEquals(expectedTotalAmount, orderItem.getTotalAmount(), "The total amount should be calculated as quantity * book.price");
    }

    @Test
    @DisplayName("Should return null for invalid quantity")
    void shouldReturnNullForInvalidQuantity() {
        // Test with negative quantity
        OrderItem negativeQuantityItem = OrderItemFactory.createOrderItemFactory(-1, book, order, OrderStatus.PENDING);
        assertNull(negativeQuantityItem, "Should return null for negative quantity.");

        // Test with zero quantity
        OrderItem zeroQuantityItem = OrderItemFactory.createOrderItemFactory(0, book, order, OrderStatus.PENDING);
        assertNull(zeroQuantityItem, "Should return null for zero quantity.");
    }

    @Test
    @DisplayName("Should return null for null objects")
    void shouldReturnNullForNullObjects() {
        // Test with null book
        OrderItem nullBookItem = OrderItemFactory.createOrderItemFactory(quantity, null, order, OrderStatus.PENDING);
        assertNull(nullBookItem, "Should return null for a null book.");

        // Test with null order
        OrderItem nullOrderItem = OrderItemFactory.createOrderItemFactory(quantity, book, null, OrderStatus.PENDING);
        assertNull(nullOrderItem, "Should return null for a null order.");

        // Test with null order status
        OrderItem nullStatusItem = OrderItemFactory.createOrderItemFactory(quantity, book, order, null);
        assertNull(nullStatusItem, "Should return null for a null order status.");
    }
}