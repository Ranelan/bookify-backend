package com.booklify.factory;

import com.booklify.domain.Book;
import com.booklify.domain.Order;
import com.booklify.domain.OrderItem;
import com.booklify.domain.enums.OrderStatus;
import com.booklify.util.Helper;

public class OrderItemFactory {

    public static OrderItem createOrderItemFactory(int quantity, double totalAmount, Book book, Order order, OrderStatus orderStatus) {
        if(!Helper.isValidQuantity(quantity)||
                !Helper.isValidPrice(totalAmount)||
                book == null ||
                order == null ||
                orderStatus == null) {
            return null;
        }

        return new OrderItem.OrderItemBuilder()
                .setQuantity(quantity)
                .setTotalAmount(totalAmount)
                .setBook(book)
                .setOrder(order)
                .setOrderStatus(orderStatus)
                .build();
    }
}

