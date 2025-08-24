package com.booklify.service.impl;

import com.booklify.domain.Payment;
import com.booklify.domain.User;
import com.booklify.domain.Order;
import com.booklify.domain.enums.PaymentStatus;
import com.booklify.repository.PaymentRepository;
import com.booklify.repository.RegularUserRepository;
import com.booklify.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepository;
    private RegularUserRepository regularUserRepository;
    private OrderRepository orderRepository;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() throws Exception {
        paymentRepository = mock(PaymentRepository.class);
        regularUserRepository = mock(RegularUserRepository.class);
        orderRepository = mock(OrderRepository.class);

        paymentService = new PaymentService();

        Field paymentRepoField = PaymentService.class.getDeclaredField("paymentRepository");
        paymentRepoField.setAccessible(true);
        paymentRepoField.set(paymentService, paymentRepository);

        Field userRepoField = PaymentService.class.getDeclaredField("regularUserRepository");
        userRepoField.setAccessible(true);
        userRepoField.set(paymentService, regularUserRepository);

        Field orderRepoField = PaymentService.class.getDeclaredField("orderRepository");
        orderRepoField.setAccessible(true);
        orderRepoField.set(paymentService, orderRepository);
    }

    // Helper methods
    private User createDummyUser() {
        return new User();
    }

    private Order createDummyOrder() {
        return new Order.OrderBuilder()
                .setOrderId(1L)
                .build();
    }

    private Payment createDummyPayment() {
        return new Payment.Builder()
                .setPaymentId(1L)
                .setUser(createDummyUser())
                .setOrder(createDummyOrder())
                .setPaymentMethod("Card")
                .setAmountPaid(100.0)
                .setPaymentStatus(PaymentStatus.COMPLETED)
                .build();
    }

    @Test
    void save_InvalidUser_ThrowsException() {
        Payment payment = createDummyPayment();
        when(regularUserRepository.existsById(anyLong())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentService.save(payment));

        assertEquals("User not found with id: " + payment.getUser().getId(), exception.getMessage());
    }

    @Test
    void save_InvalidOrder_ThrowsException() {
        Payment payment = createDummyPayment();
        when(regularUserRepository.existsById(anyLong())).thenReturn(true);
        when(orderRepository.existsById(anyLong())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentService.save(payment));

        assertEquals("Order not found with id: " + payment.getOrder().getOrderId(), exception.getMessage());
    }

    @Test
    void findById_PaymentNotFound_ThrowsException() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentService.findById(1L));

        assertEquals("Payment not found with id: 1", exception.getMessage());
    }

    @Test
    void processRefund_ValidPayment_ReturnsTrue() {
        Payment payment = createDummyPayment();
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        boolean result = paymentService.processRefund(1L, 50.0);

        assertTrue(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void processRefund_InvalidAmount_ThrowsException() {
        Payment payment = createDummyPayment();
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentService.processRefund(1L, 200.0));

        assertEquals("Refund amount must be positive and ≤ original payment", exception.getMessage());
    }
}

