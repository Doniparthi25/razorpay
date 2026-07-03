package com.codingshuttle.razorpay.razorpay.payment.gateway.config;

import com.codingshuttle.razorpay.razorpay.common.enums.PaymentMethod;
import com.codingshuttle.razorpay.razorpay.payment.gateway.PaymentAdapter;
import com.codingshuttle.razorpay.razorpay.payment.gateway.adapter.CardPaymentAdapter;
import com.codingshuttle.razorpay.razorpay.payment.gateway.adapter.NetBankingAdapter;
import com.codingshuttle.razorpay.razorpay.payment.gateway.adapter.UpiPaymentAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentAdapterConfig {

    @Bean
    private Map<PaymentMethod,PaymentAdapter> paymentAdapterMap() {
return Map.of(
        PaymentMethod.CARD,new CardPaymentAdapter(),
        PaymentMethod.NETBANKING,new NetBankingAdapter(),
        PaymentMethod.UPI,new UpiPaymentAdapter()
        );
    }
}
