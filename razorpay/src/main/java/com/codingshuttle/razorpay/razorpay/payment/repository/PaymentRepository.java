package com.codingshuttle.razorpay.razorpay.payment.repository;

import com.codingshuttle.razorpay.razorpay.payment.entity.OrderRecord;
import com.codingshuttle.razorpay.razorpay.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByOrder_id(OrderRecord order);

    Optional<Payment> findByIdAndMerchantId(UUID paymentId, UUID merchantId);
}
