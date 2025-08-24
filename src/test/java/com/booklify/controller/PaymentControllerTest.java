package com.booklify.controller;

import com.booklify.domain.Payment;
import com.booklify.domain.User;
import com.booklify.domain.Order;
import com.booklify.domain.enums.PaymentStatus;
import com.booklify.service.IPaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    private IPaymentService paymentService;
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        paymentService = mock(IPaymentService.class);
        paymentService = mock(IPaymentService.class);
        paymentController = new PaymentController(paymentService);

    }

    private Payment createDummyPayment() {
        return new Payment.Builder()
                .setPaymentId(1L)
                .setUser(new User())
                .setOrder(new Order())
                .setPaymentMethod("Card")
                .setAmountPaid(100.0)
                .setPaymentStatus(PaymentStatus.COMPLETED)
                .build();
    }

    @Test
    void createPayment_NullInput_ReturnsBadRequest() {
        ResponseEntity<Payment> response = paymentController.createPayment(null);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void createPayment_ValidInput_ReturnsCreated() {
        Payment payment = createDummyPayment();
        when(paymentService.save(payment)).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.createPayment(payment);

        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void getPaymentById_NotFound_ReturnsNotFound() {
        when(paymentService.findById(1L)).thenThrow(new RuntimeException("Payment not found with id: 1"));

        ResponseEntity<Payment> response = paymentController.getPaymentById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getAllPayments_ReturnsOk() {
        Payment payment = createDummyPayment();
        when(paymentService.getAll()).thenReturn(List.of(payment));

        ResponseEntity<List<Payment>> response = paymentController.getAllPayments();

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void refundPayment_InvalidAmount_ReturnsBadRequest() {
        ResponseEntity<Void> response = paymentController.refundPayment(1L, -50.0);
        assertEquals(400, response.getStatusCodeValue());
    }
}
