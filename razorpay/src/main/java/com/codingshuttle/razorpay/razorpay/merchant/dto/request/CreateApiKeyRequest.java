package com.codingshuttle.razorpay.razorpay.merchant.dto.request;

import com.codingshuttle.razorpay.razorpay.common.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}
