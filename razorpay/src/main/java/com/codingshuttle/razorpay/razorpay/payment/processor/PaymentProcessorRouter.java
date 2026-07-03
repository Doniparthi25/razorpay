package com.codingshuttle.razorpay.razorpay.payment.processor;

import com.codingshuttle.razorpay.razorpay.common.enums.PaymentMethod;
import com.codingshuttle.razorpay.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.codingshuttle.razorpay.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class PaymentProcessorRouter {

    private final Map<PaymentMethod,PaymentProcessor> paymentProcessors;

    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        PaymentProcessor processor =paymentProcessors.get(request.method());
            if (processor == null) {
                throw new IllegalArgumentException("No payment processor registered for method: "+request.method());
            }
        return processor.charge(request);
    }
}
