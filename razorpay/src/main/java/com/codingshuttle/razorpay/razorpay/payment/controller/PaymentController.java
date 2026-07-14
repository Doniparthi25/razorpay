package com.codingshuttle.razorpay.razorpay.payment.controller;

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

    private final PaymentService paymentService;
    UUID merchantId = UUID.fromString("5647437e-3821-4e11-a7f5-9b42bd7866ee");

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody PaymentInitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiate(merchantId,request));
    }

    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponse> capture (@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.capture(merchantId,paymentId));
    }

}
