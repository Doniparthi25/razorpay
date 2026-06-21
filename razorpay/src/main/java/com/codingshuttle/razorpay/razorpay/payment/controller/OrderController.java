package com.codingshuttle.razorpay.razorpay.payment.controller;

import com.codingshuttle.razorpay.razorpay.payment.dto.request.CreateOrderRequest;
import com.codingshuttle.razorpay.razorpay.payment.dto.response.OrderResponse;
import com.codingshuttle.razorpay.razorpay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;
    UUID merchantId = UUID.fromString("835baa59-fcc2-4bd7-bc09-beb4628f3565");

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.create(merchantId, request));
    }
}
