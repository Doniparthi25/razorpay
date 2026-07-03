package com.codingshuttle.razorpay.razorpay.payment.processor;

import com.codingshuttle.razorpay.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.codingshuttle.razorpay.razorpay.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);
}
