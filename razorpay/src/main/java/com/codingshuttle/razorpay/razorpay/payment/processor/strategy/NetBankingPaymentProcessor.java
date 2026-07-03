package com.codingshuttle.razorpay.razorpay.payment.processor.strategy;

import com.codingshuttle.razorpay.razorpay.payment.processor.PaymentProcessor;
import com.codingshuttle.razorpay.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.codingshuttle.razorpay.razorpay.payment.processor.dto.PaymentProcessorResponse;

public class NetBankingPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }
}
