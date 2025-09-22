package com.booklify.service.impl;

import com.booklify.domain.Payment;
import com.booklify.domain.enums.PaymentStatus;
import com.booklify.repository.PaymentRepository;
import com.booklify.repository.RegularUserRepository;
import com.booklify.repository.OrderRepository;
import com.booklify.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RegularUserRepository regularUserRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Payment save(Payment payment) {
        validateUserAndOrder(payment);

        // Always fetch the order and set the amount from the order's total
        var order = orderRepository.findById(payment.getOrder().getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + payment.getOrder().getOrderId()));

        // Assume you have a getTotalAmount() method on Order
        BigDecimal totalAmount = order.getTotalAmount();

        Payment paymentToSave = new Payment.Builder()
                .copy(payment)
                .setAmountPaid(totalAmount)
                .setPaymentDate(payment.getPaymentDate() != null ? payment.getPaymentDate() : java.time.LocalDateTime.now())
                .setPaymentStatus(com.booklify.domain.enums.PaymentStatus.COMPLETED)
                .build();

        return paymentRepository.save(paymentToSave);
    }

    @Override
    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }

    @Override
    public Payment update(Payment payment) {
        Payment existing = findById(payment.getPaymentId());

        // Always fetch the order and set the amount from the order's total
        var order = orderRepository.findById(payment.getOrder().getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + payment.getOrder().getOrderId()));

        BigDecimal totalAmount = order.getTotalAmount();

        Payment updatedPayment = new Payment.Builder()
                .copy(existing)
                .setUser(regularUserRepository.findById(payment.getUser().getId())
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + payment.getUser().getId())))
                .setOrder(order)
                .setPaymentMethod(payment.getPaymentMethod())
                .setAmountPaid(totalAmount) // Always recalculate from order
                .setPaymentStatus(payment.getPaymentStatus())
                .setPaymentDate(payment.getPaymentDate() != null ? payment.getPaymentDate() : java.time.LocalDateTime.now())
                .build();

        return paymentRepository.save(updatedPayment);
    }

    @Override
    public boolean deleteById(Long id) {
        Payment payment = findById(id);
        paymentRepository.delete(payment);
        return true;
    }

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> findByUser(Long userId) {
        return paymentRepository.findByUser_Id(userId);
    }

    @Override
    public List<Payment> findByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status);
    }

    @Override
    public boolean processRefund(Long paymentId, BigDecimal amount) {
        Payment payment = findById(paymentId);

        if (payment.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Only completed payments can be refunded");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(payment.getAmountPaid()) > 0) {
            throw new RuntimeException("Refund amount must be positive and ≤ original payment");
        }

        Payment refundedPayment = new Payment.Builder()
                .copy(payment)
                .setPaymentStatus(PaymentStatus.REFUNDED)
                .setPaymentDate(payment.getPaymentDate() != null ? payment.getPaymentDate() : java.time.LocalDateTime.now())
                .build();

        paymentRepository.save(refundedPayment);
        return true;
    }

    private void validateUserAndOrder(Payment payment) {
        if (!regularUserRepository.existsById(payment.getUser().getId())) {
            throw new RuntimeException("User not found with id: " + payment.getUser().getId());
        }
        if (!orderRepository.existsById(payment.getOrder().getOrderId())) {
            throw new RuntimeException("Order not found with id: " + payment.getOrder().getOrderId());
        }
    }
}
