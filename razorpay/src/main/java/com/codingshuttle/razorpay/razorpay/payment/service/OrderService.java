package com.codingshuttle.razorpay.razorpay.payment.service;

import com.codingshuttle.razorpay.razorpay.payment.dto.request.CreateOrderRequest;
import com.codingshuttle.razorpay.razorpay.payment.dto.response.OrderResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface OrderService {
     OrderResponse create(UUID merchantId,  CreateOrderRequest request);
}
