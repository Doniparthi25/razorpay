package com.codingshuttle.razorpay.razorpay.common.enums;

public enum PaymentEvent {
    AUTHORIZING_ATTEMPT,
    AUTHORIZE_SUCCESS,
    AUTHORIZE_FAIL,
    CAPTURE_ATTEMPT,
    CAPTURE_REQUEST,
    CAPTURE_FAIL,
    CAPTURE_SUCCESS,
    REFUND_INIT,
    REFUND_COMPLETED,
    SETTLE,
    CANCEL,
    CAPTURE_TIMEOUT
}
