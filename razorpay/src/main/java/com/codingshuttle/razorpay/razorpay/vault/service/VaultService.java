package com.codingshuttle.razorpay.razorpay.vault.service;


import com.codingshuttle.razorpay.razorpay.common.entity.Money;
import com.codingshuttle.razorpay.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.codingshuttle.razorpay.razorpay.vault.dto.request.TokenizeRequest;
import com.codingshuttle.razorpay.razorpay.vault.dto.response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {

    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails);
}
