package com.codingshuttle.razorpay.razorpay.payment.api;

import com.codingshuttle.razorpay.razorpay.payment.entity.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentLookupService {
    List<Payment> findUnsettledCapturedPayments(UUID merchantId);
}
