package com.booklify.domain;

import com.booklify.domain.enums.OrderStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    private int quantity;

    // This will now be a calculated field
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;


    @ManyToOne(optional = false, fetch = FetchType.LAZY) // Using LAZY fetch is generally better for performance
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Order order;

    // JPA requirement
    public OrderItem() {}

    // Builder-based constructor
    private OrderItem(OrderItemBuilder orderItemBuilder) {
        this.orderItemId = orderItemBuilder.orderItemId;
        this.quantity = orderItemBuilder.quantity;
        this.orderStatus = orderItemBuilder.orderStatus;
        this.book = orderItemBuilder.book;
        this.order = orderItemBuilder.order;
        // The totalAmount is now set by the builder's calculation
        this.totalAmount = orderItemBuilder.totalAmount;
    }

    // --- JPA Lifecycle Callback ---
    // This method will automatically run before the entity is saved for the first time or updated.
    // It's a safety net to ensure the totalAmount is always correct.
    @PrePersist
    @PreUpdate
    public void calculateTotalAmount() {
        if (getBook() != null && getQuantity() > 0) {
            // Assuming your Book entity has a getPrice() method
            this.setTotalAmount(getBook().getPrice() * getQuantity());
        } else {
            this.setTotalAmount(0.0);
        }
    }


    // Getters
    public Long getOrderItemId() {
        return orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Book getBook() {
        return book;
    }

    public Order getOrder() {
        return order;
    }

    public double getPrice() {
        return book != null ? book.getPrice() : 0.0;
    }

    // Add a setter for the calculated field, mainly for the callback to use
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


    // ... toString method ...
    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", orderStatus=" + orderStatus +
                ", book=" + (book != null ? book.getBookID() : "null") +
                ", order=" + (order != null ? order.getOrderId() : "null") +
                '}';
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    // --- Updated Builder Class ---
    public static class OrderItemBuilder {
        private Long orderItemId;
        private int quantity;
        private double totalAmount; // Keep this private to store the calculated value
        private OrderStatus orderStatus;
        private Book book;
        private Order order;

        public OrderItemBuilder setOrderItemId(Long orderItemId) {
            this.orderItemId = orderItemId;
            return this;
        }

        public OrderItemBuilder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        // REMOVED: Do not allow external setting of the total amount.
        // public OrderItemBuilder setTotalAmount(double totalAmount) { ... }

        public OrderItemBuilder setOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderItemBuilder setBook(Book book) {
            this.book = book;
            return this;
        }

        public OrderItemBuilder setOrder(Order order) {
            this.order = order;
            return this;
        }

        public OrderItemBuilder copy(OrderItem orderItem) {
            this.orderItemId = orderItem.orderItemId;
            this.quantity = orderItem.quantity;
            this.orderStatus = orderItem.orderStatus;
            this.book = orderItem.book;
            this.order = orderItem.order;
            // Recalculate when copying to be safe
            if (this.book != null && this.quantity > 0) {
                this.totalAmount = this.book.getPrice() * this.quantity;
            }
            return this;
        }

        public OrderItem build() {
            // Validation and Calculation logic is now here
            if (book == null) {
                throw new IllegalStateException("Book cannot be null when building an OrderItem.");
            }
            if (quantity <= 0) {
                throw new IllegalStateException("Quantity must be greater than 0.");
            }

            // THE KEY CHANGE: Calculate the total amount automaticall
            this.totalAmount = book.getPrice() * this.quantity;

            return new OrderItem(this);
        }
    }
}