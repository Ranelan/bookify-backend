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
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "bookid", nullable = false)
    private Long bookid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Default constructor (JPA requirement)
    public OrderItem() {}

    // Builder-based constructor
    private OrderItem(OrderItemBuilder orderItemBuilder) {
        this.orderItemId = orderItemBuilder.orderItemId;
        this.quantity = orderItemBuilder.quantity;
        this.totalAmount = orderItemBuilder.totalAmount;
        this.orderStatus = orderItemBuilder.orderStatus;
        this.book = orderItemBuilder.book;
        this.order = orderItemBuilder.order;
        this.bookid = orderItemBuilder.book != null ? orderItemBuilder.book.getBookID() : null;
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

    public Long getBookid() {
        return bookid;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", orderStatus=" + orderStatus +
                ", bookid=" + bookid +
                ", book=" + book +
                ", order=" + order +
                '}';
    }

    // Builder class
    public static class OrderItemBuilder {
        private Long orderItemId;
        private int quantity;
        private double totalAmount;
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

        public OrderItemBuilder setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

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
            this.totalAmount = orderItem.totalAmount;
            this.orderStatus = orderItem.orderStatus;
            this.book = orderItem.book;
            this.order = orderItem.order;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
