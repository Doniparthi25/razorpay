package com.codingshuttle.razorpay.razorpay.payment.repository;

import com.codingshuttle.razorpay.razorpay.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
