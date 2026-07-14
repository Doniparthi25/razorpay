package com.codingshuttle.razorpay.razorpay.payment.statemachine;

import com.codingshuttle.razorpay.razorpay.common.enums.PaymentActor;
import com.codingshuttle.razorpay.razorpay.common.enums.PaymentEvent;
import com.codingshuttle.razorpay.razorpay.common.enums.PaymentStatus;
import com.codingshuttle.razorpay.razorpay.payment.entity.Payment;
import com.codingshuttle.razorpay.razorpay.payment.entity.PaymentTransitionLog;
import com.codingshuttle.razorpay.razorpay.payment.repository.PaymentTransitionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentTransitionService {

    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine paymentStateMachine;


    public PaymentStatus apply(Payment payment, PaymentEvent event) {
        PaymentStatus next = paymentStateMachine.transition(payment.getStatus(),event);
        PaymentTransitionLog log = PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(payment.getStatus())
                .toStatus(next)
                .event(event)
                .actor(PaymentActor.SYSTEM)
                .occurredAt(LocalDateTime.now())
                .build();

        payment.setStatus(next);
        paymentTransitionLogRepository.save(log);
        return next;
    }
}
