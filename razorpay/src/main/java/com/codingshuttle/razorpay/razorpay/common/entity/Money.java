package com.codingshuttle.razorpay.razorpay.common.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Money {
    private String currency;
    private int amountUnits;

    private Money(int amountUnits,String currency) {
        this.currency = currency;
        this.amountUnits = amountUnits;
    }

    public static Money of(int amountUnits, String currency) {
        return new Money(amountUnits, currency);
    }

    public static Money inr(int amountUnits) {
        return new Money(amountUnits, "INR");
    }

    public Money subtract(Money other) {
        if(!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract money with different currencies");
        }
        return new Money(this.amountUnits - other.amountUnits, this.currency);
    }

    public Money add(Money other) {
        if(!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return new Money(this.amountUnits+other.amountUnits , this.currency);
    }
}
