package com.codingshuttle.razorpay.razorpay.vault.dto.response;

import com.codingshuttle.razorpay.razorpay.common.enums.CardBrand;

public record TokenizeResponse(
        String token,
        String lastFour,
        CardBrand brand,
        Integer expiryMonth,
        Integer expiryYear
) {
}
