package com.codingshuttle.razorpay.razorpay.merchant.dto.response;

import com.codingshuttle.razorpay.razorpay.common.enums.Environment;

import java.util.UUID;

public record ApiKeyCreateResponse(
        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}
