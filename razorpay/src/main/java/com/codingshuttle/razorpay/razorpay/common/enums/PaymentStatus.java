package com.codingshuttle.razorpay.razorpay.common.enums;

public enum PaymentStatus {
    CREATED,
    AUTHORIZING,
    AUTHORIZED,
    CAPTURING,
    CAPTURED,
    FAILED,
    CANCELLED,
    REFUND,
    PARTIALLY_REFUNDED,
    SETTLED,
    AUTH_EXPIRED,
}
