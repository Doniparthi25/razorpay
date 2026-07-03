package com.codingshuttle.razorpay.razorpay.payment.gateway;

import com.codingshuttle.razorpay.razorpay.payment.dto.response.PaymentResponse;
import com.codingshuttle.razorpay.razorpay.payment.gateway.dto.PaymentRequest;
import com.codingshuttle.razorpay.razorpay.payment.gateway.dto.PaymentResult;

public interface PaymentAdapter {
    PaymentResult initiate(PaymentRequest request);
}
