package com.booklify.factory;

import com.booklify.domain.Book;
import com.booklify.domain.Order;
import com.booklify.domain.OrderItem;
import com.booklify.domain.enums.OrderStatus;
import com.booklify.util.Helper;

public class OrderItemFactory {

    /**
     * Creates a new OrderItem. The total amount is calculated automatically
     * based on the book's price and the quantity.
     *
     * @param quantity    The number of books for this item.
     * @param book        The Book being ordered. Must not be null.
     * @param order       The parent Order. Must not be null.
     * @param orderStatus The initial status of the order item.
     * @return A new OrderItem object, or null if the input is invalid.
     */
    public static OrderItem createOrderItemFactory(int quantity, Book book, Order order, OrderStatus orderStatus) {
        // Validation has been simplified: we no longer validate a totalAmount.
        if (!Helper.isValidQuantity(quantity) ||
                book == null ||
                order == null ||
                orderStatus == null) {
            // Returning null for invalid input, as per the original design.
            return null;
        }

        return new OrderItem.OrderItemBuilder()
                .setQuantity(quantity)
                .setBook(book)
                .setOrder(order)
                .setOrderStatus(orderStatus)
                .build();
    }
}