package com.codingshuttle.razorpay.razorpay.operations.entity;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class SettlemetPaymentId {

    private UUID settlementId;

    private UUID paymentId;
}
