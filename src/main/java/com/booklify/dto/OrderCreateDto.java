package com.booklify.dto;

import java.util.List;

public class OrderCreateDto {
    private Long regularUserId;
    private Long shippingAddressId;
    private List<OrderItemCreateDto> orderItems;

    // Getters and setters
    public Long getRegularUserId() { return regularUserId; }
    public void setRegularUserId(Long regularUserId) { this.regularUserId = regularUserId; }
    public Long getShippingAddressId() { return shippingAddressId; }
    public void setShippingAddressId(Long shippingAddressId) { this.shippingAddressId = shippingAddressId; }
    public List<OrderItemCreateDto> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemCreateDto> orderItems) { this.orderItems = orderItems; }

    public static class OrderItemCreateDto {
        private Long bookId;
        private int quantity;
        private String orderStatus;
        // Getters and setters
        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public String getOrderStatus() { return orderStatus; }
        public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    }
}

