package com.booklify.domain;

import com.booklify.domain.enums.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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


    public Order() {
        // Default constructor
    }

    public Order(Long orderId, LocalDateTime orderDate,  RegularUser regularUser) {  // Constructor with parameters
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.regularUser = regularUser;
    }

    public Order(OrderBuilder orderBuilder) {  // Constructor using OrderBuilder
        this.orderId = orderBuilder.orderId;
        this.orderDate = orderBuilder.orderDate;
        this.regularUser = orderBuilder.regularUser;
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

    @Override
    public String toString() {  // Override toString method for better readability
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate=" + orderDate +
                ", regularUser=" + regularUser +
                '}';
    }

    public static class OrderBuilder {  // Builder class for Order
        private Long orderId;
        private LocalDateTime orderDate;
        private RegularUser regularUser;

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

        public OrderBuilder copy(Order order) {
            this.orderId = order.orderId;
            this.orderDate = order.orderDate;
            this.regularUser = order.regularUser;
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
