package com.codingshuttle.razorpay.razorpay.payment.controller;

import com.codingshuttle.razorpay.razorpay.merchant.security.MerchantContext;
import com.codingshuttle.razorpay.razorpay.payment.dto.request.PaymentInitRequest;
import com.codingshuttle.razorpay.razorpay.payment.dto.response.PaymentResponse;
import com.codingshuttle.razorpay.razorpay.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/v1/payments")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final MerchantContext merchantContext;

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody PaymentInitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiate(merchantContext.getMerchantId(),request));
    }

    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponse> capture (@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.capture(merchantContext.getMerchantId(),paymentId));
    }

}
