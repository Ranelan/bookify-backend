package com.booklify.repository;

import com.booklify.domain.Payment;
import com.booklify.domain.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


    List<Payment> findByUser_Id(Long userId);
    List<Payment> findByOrder_OrderId(Long orderId);
    List<Payment> findByPaymentStatus(PaymentStatus status);
    List<Payment> findByPaymentMethod(String method);

    @Query("SELECT SUM(p.amountPaid) FROM Payment p WHERE p.user.id = :userId")
    Double calculateTotalAmountByUser(Long userId);
}