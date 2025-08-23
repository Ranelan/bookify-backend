package com.booklify.service.impl;

import com.booklify.domain.Payment;
import com.booklify.domain.enums.PaymentStatus;
import com.booklify.repository.PaymentRepository;
import com.booklify.repository.RegularUserRepository;
import com.booklify.repository.OrderRepository;
import com.booklify.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return paymentRepository.save(payment);
    }

    @Override
    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }

    @Override
    public Payment update(Payment payment) {
        Payment existing = findById(payment.getPaymentId());

        Payment updatedPayment = new Payment.Builder()
                .copy(existing)
                .setUser(regularUserRepository.findById(payment.getUser().getId())
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + payment.getUser().getId())))
                .setOrder(orderRepository.findById(payment.getOrder().getOrderId())
                        .orElseThrow(() -> new RuntimeException("Order not found with id: " + payment.getOrder().getOrderId())))
                .setPaymentMethod(payment.getPaymentMethod())
                .setAmountPaid(payment.getAmountPaid())
                .setPaymentStatus(payment.getPaymentStatus())
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
    public boolean processRefund(Long paymentId, double amount) {
        Payment payment = findById(paymentId);

        if (payment.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Only completed payments can be refunded");
        }
        if (amount <= 0 || amount > payment.getAmountPaid()) {
            throw new RuntimeException("Refund amount must be positive and ≤ original payment");
        }

        Payment refundedPayment = new Payment.Builder()
                .copy(payment)
                .setPaymentStatus(PaymentStatus.REFUNDED)
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

