package com.codingshuttle.razorpay.razorpay.merchant.dto.response;

import com.codingshuttle.razorpay.razorpay.common.enums.BusinessType;
import com.codingshuttle.razorpay.razorpay.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus
) {
}
