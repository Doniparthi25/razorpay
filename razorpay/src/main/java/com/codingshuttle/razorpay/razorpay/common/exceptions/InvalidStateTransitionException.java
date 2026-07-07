package com.codingshuttle.razorpay.razorpay.common.exceptions;

import com.codingshuttle.razorpay.razorpay.common.util.RandomizerUtil;
import lombok.Getter;

@Getter
public class InvalidStateTransitionException extends RuntimeException {
    private final String fromState;
    private final String toEvent;

    public InvalidStateTransitionException(String fromState, String event) {
        super("Invalid state transition form " + fromState + " with event " + event);
        this.fromState = fromState;
        this.toEvent = event;

    }
}
