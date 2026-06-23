package com.codingshuttle.razorpay.razorpay.payment.service;

import com.codingshuttle.razorpay.razorpay.payment.dto.request.CreateOrderRequest;
import com.codingshuttle.razorpay.razorpay.payment.dto.response.OrderResponse;
import com.codingshuttle.razorpay.razorpay.payment.dto.response.PaymentResponse;
import java.util.List;
import java.util.UUID;

public interface OrderService {
     OrderResponse create(UUID merchantId,  CreateOrderRequest request);

     OrderResponse getById(UUID merchanId, UUID orderId);

     OrderResponse cancel(UUID merchantId,UUID orderId);

     List<PaymentResponse> listPayments(UUID merchantId,UUID orderId);
}
