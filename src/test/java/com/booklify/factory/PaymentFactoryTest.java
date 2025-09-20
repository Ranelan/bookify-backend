package com.booklify.factory;

import com.booklify.domain.Payment;
import com.booklify.domain.User;
import com.booklify.domain.Order;
import com.booklify.domain.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentFactoryTest {


    private User createDummyUser() {
        User user = new User();
        return user;
    }


    private Order createDummyOrder() {
        Order order = new Order();
        return order;
    }

    @Test
    void createPayment_NullUser_ReturnsNull() {
        Order order = createDummyOrder();
        Payment payment = PaymentFactory.createPayment(null, order, "Card", BigDecimal.valueOf(300.00), PaymentStatus.COMPLETED);
        assertNull(payment);
    }

    @Test
    void createPayment_NullOrder_ReturnsNull() {
        User user = createDummyUser();
        Payment payment = PaymentFactory.createPayment(user, null, "Card", BigDecimal.valueOf(100.0), PaymentStatus.COMPLETED);
        assertNull(payment);
    }

    @Test
    void createPayment_InvalidAmount_ReturnsNull() {
        User user = createDummyUser();
        Order order = createDummyOrder();
        Payment payment = PaymentFactory.createPayment(user, order, "Card", BigDecimal.valueOf(-50.0), PaymentStatus.COMPLETED);
        assertNull(payment);
    }

    @Test
    void createPayment_NullPaymentStatus_ReturnsNull() {
        User user = createDummyUser();
        Order order = createDummyOrder();
        Payment payment = PaymentFactory.createPayment(user, order, "Card", BigDecimal.valueOf(100.0), null);
        assertNull(payment);
    }

    @Test
    void createPayment_ValidData_ReturnsPayment() {
        User user = createDummyUser();
        Order order = createDummyOrder();
        Payment payment = PaymentFactory.createPayment(user, order, "Card", BigDecimal.valueOf(100.0), PaymentStatus.COMPLETED);
        assertNotNull(payment);
    }
}
