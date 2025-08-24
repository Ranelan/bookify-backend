package com.booklify.domain;

import com.booklify.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false, length = 50)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false, precision = 10, scale = 2)
    private double amountPaid;

    @Column(name = "payment_date", nullable = false, updatable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();


    public Payment() {

    }

    private Payment(Builder builder) {
        this.paymentId = builder.paymentId;
        this.user = builder.user;
        this.order = builder.order;
        this.paymentMethod = builder.paymentMethod;
        this.paymentStatus = builder.paymentStatus;
        this.amountPaid = builder.amountPaid;
        this.paymentDate = builder.paymentDate;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public User getUser() {
        return user;
    }

    public Order getOrder() {
        return order;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public User getRegularUser() {
        return user;
    }

    public static class Builder {
        private Long paymentId;
        private User user;
        private Order order;
        private String paymentMethod;
        private PaymentStatus paymentStatus;
        private double amountPaid;
        private LocalDateTime paymentDate;

        public Builder setPaymentId(Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setRegularUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setOrder(Order order) {
            this.order = order;
            return this;
        }

        public Builder setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder setPaymentStatus(PaymentStatus paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public Builder setAmountPaid(double amountPaid) {
            this.amountPaid = amountPaid;
            return this;
        }

        public Builder setPaymentDate(LocalDateTime paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        public Builder copy(Payment payment) {
            this.paymentId = payment.paymentId;
            this.user = payment.user;
            this.order = payment.order;
            this.paymentMethod = payment.paymentMethod;
            this.paymentStatus = payment.paymentStatus;
            this.amountPaid = payment.amountPaid;
            this.paymentDate = payment.paymentDate;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }
}
