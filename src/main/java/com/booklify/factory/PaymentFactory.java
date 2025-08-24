package com.booklify.factory;

import com.booklify.domain.Payment;
import com.booklify.domain.User;
import com.booklify.domain.Order;
import com.booklify.domain.enums.PaymentStatus;
import com.booklify.util.Helper;

import java.math.BigDecimal;

public class PaymentFactory {


    public static Payment createPayment(User user,
                                        Order order,
                                        String paymentMethod,
                                        BigDecimal amount,
                                        PaymentStatus paymentStatus) {

        if (user == null || order == null ||
                Helper.isNullOrEmpty(paymentMethod) ||
                amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 ||
                paymentStatus == null) {
            return null;
        }

        return new Payment.Builder()
                .setUser(user)
                .setOrder(order)
                .setPaymentMethod(paymentMethod)
                .setAmountPaid(amount)
                .setPaymentStatus(paymentStatus)
                .build();
    }
}