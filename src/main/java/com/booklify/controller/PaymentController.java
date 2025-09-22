package com.booklify.controller;

import com.booklify.domain.Order;
import com.booklify.domain.Payment;
import com.booklify.domain.RegularUser;
import com.booklify.domain.enums.PaymentStatus;
import com.booklify.dto.PaymentRequestDTO;
import com.booklify.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;


    @Autowired
    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentRequestDTO paymentRequest) {
        if (paymentRequest == null) {
            return ResponseEntity.badRequest().build();
        }
        // Build a Payment object with only user, order, and method
        Payment payment = new Payment.Builder()
                .setUser(new RegularUser.RegularUserBuilder().setId(paymentRequest.getUserId()).build())
                .setOrder(new Order.OrderBuilder().setOrderId(paymentRequest.getOrderId()).build())
                .setPaymentMethod(paymentRequest.getPaymentMethod())
                .setPaymentStatus(PaymentStatus.PENDING)
                .build();

        Payment savedPayment = paymentService.save(payment);
        return ResponseEntity.status(201).body(savedPayment);
    }

    @PutMapping("/update/{paymentId}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long paymentId, @RequestBody Payment payment) {
        if (payment == null || paymentId == null) {
            return ResponseEntity.badRequest().build();
        }

        Payment updatedPayment = new Payment.Builder()
                .copy(payment)
                .setPaymentId(paymentId)
                .build();

        updatedPayment = paymentService.update(updatedPayment);
        if (updatedPayment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/delete/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long paymentId) {
        if (paymentId == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean isDeleted = paymentService.deleteById(paymentId);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getById/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long paymentId) {
        if (paymentId == null) {
            return ResponseEntity.badRequest().build();
        }

        Payment payment = paymentService.findById(paymentId);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(payment);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/getByUser/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUser(@PathVariable Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Payment> payments = paymentService.findByUser(userId);
        if (payments == null || payments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        if (status == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Payment> payments = paymentService.findByStatus(status);
        if (payments == null || payments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(payments);
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<Void> refundPayment(@PathVariable Long paymentId, @RequestParam BigDecimal amount) {
        if (paymentId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            paymentService.processRefund(paymentId, amount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
