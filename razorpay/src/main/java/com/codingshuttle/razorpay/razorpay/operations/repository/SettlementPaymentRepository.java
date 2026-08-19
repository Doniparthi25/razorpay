package com.codingshuttle.razorpay.razorpay.operations.repository;

import com.codingshuttle.razorpay.razorpay.operations.entity.SettlementPayment;
import com.codingshuttle.razorpay.razorpay.operations.entity.SettlementPaymentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SettlementPaymentRepository extends JpaRepository<SettlementPayment, SettlementPaymentId> {
}
