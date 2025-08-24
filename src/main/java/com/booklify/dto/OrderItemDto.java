package com.booklify.dto;

import com.booklify.domain.OrderItem;
import com.booklify.domain.enums.OrderStatus;

public class OrderItemDto {
    private Long orderItemId;
    private int quantity;
    private double totalAmount;
    private OrderStatus orderStatus;
    private Long bookId;
    private String bookTitle; // <-- Add this
    private Long orderId;

    // Getters and setters for all fields, including bookTitle


    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public static OrderItemDto fromEntity(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setOrderItemId(orderItem.getOrderItemId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setTotalAmount(orderItem.getTotalAmount());
        dto.setOrderStatus(orderItem.getOrderStatus());
        dto.setBookId(orderItem.getBook() != null ? orderItem.getBook().getBookID() : null);
        dto.setBookTitle(orderItem.getBook() != null ? orderItem.getBook().getTitle() : null); // <-- Set bookTitle
        dto.setOrderId(orderItem.getOrder() != null ? orderItem.getOrder().getOrderId() : null);
        return dto;
    }
}
