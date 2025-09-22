package com.booklify.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = true)
    private LocalDateTime orderDate;

    @ManyToOne
    private RegularUser regularUser;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Order() {
        // Default constructor
    }

    public Order(Long orderId, LocalDateTime orderDate,  RegularUser regularUser ) {  // Constructor with parameters
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.regularUser = regularUser;
    }

    public Order(OrderBuilder orderBuilder) {
        this.orderId = orderBuilder.orderId;
        this.orderDate = orderBuilder.orderDate;
        this.regularUser = orderBuilder.regularUser;
        this.shippingAddress = orderBuilder.shippingAddress;
        this.orderItems = orderBuilder.orderItems != null ? orderBuilder.orderItems : new ArrayList<>();
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public RegularUser getRegularUser() {
        return regularUser;
    }

    public BigDecimal getTotalAmount() {
        return orderItems.stream()
                .map(item -> BigDecimal.valueOf(item.getTotalAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Override
    public String toString() {  // Override toString method for better readability
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate=" + orderDate +
                ", regularUser=" + regularUser +
                '}';
    }

    public void setOrderDate(LocalDateTime now) {
        this.orderDate = now;
    }

    public void setRegularUser(RegularUser user) {
        this.regularUser = user;
    }


    public static class OrderBuilder {  // Builder class for Order
        private Long orderId;
        private LocalDateTime orderDate;
        private RegularUser regularUser;
        private Address shippingAddress;
        private List<OrderItem> orderItems;

        public OrderBuilder setOrderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderBuilder setOrderDate(LocalDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public OrderBuilder setRegularUser(RegularUser regularUser) {
            this.regularUser = regularUser;
            return this;
        }

        public OrderBuilder setShippingAddress(Address shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public OrderBuilder setOrderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
            return this;
        }

        public OrderBuilder copy(Order order) {
            this.orderId = order.orderId;
            this.orderDate = order.orderDate;
            this.regularUser = order.regularUser;
            this.shippingAddress = order.shippingAddress;
            this.orderItems = order.getOrderItems(); // Copy order items as well
            return this;
        }


        public Order build() {
            if(this.orderDate == null){
                this.orderDate = LocalDateTime.now(); // Set current time if orderDate is not provided
            }
            return new Order(this);
        }
    }
}
