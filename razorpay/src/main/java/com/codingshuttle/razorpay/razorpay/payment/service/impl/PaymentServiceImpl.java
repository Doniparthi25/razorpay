package com.codingshuttle.razorpay.razorpay.payment.service.impl;

import com.codingshuttle.razorpay.razorpay.common.enums.OrderStatus;
import com.codingshuttle.razorpay.razorpay.common.enums.PaymentEvent;
import com.codingshuttle.razorpay.razorpay.common.enums.PaymentMethod;
import com.codingshuttle.razorpay.razorpay.common.enums.PaymentStatus;
import com.codingshuttle.razorpay.razorpay.common.exceptions.BusinessRuleViolationException;
import com.codingshuttle.razorpay.razorpay.common.exceptions.ResourceNotFoundException;
import com.codingshuttle.razorpay.razorpay.payment.dto.request.PaymentInitRequest;
import com.codingshuttle.razorpay.razorpay.payment.dto.response.PaymentResponse;
import com.codingshuttle.razorpay.razorpay.payment.entity.OrderRecord;
import com.codingshuttle.razorpay.razorpay.payment.entity.Payment;
import com.codingshuttle.razorpay.razorpay.payment.gateway.PaymentGatewayRouter;
import com.codingshuttle.razorpay.razorpay.payment.gateway.dto.PaymentRequest;
import com.codingshuttle.razorpay.razorpay.payment.gateway.dto.PaymentResult;
import com.codingshuttle.razorpay.razorpay.payment.mapper.PaymentMapper;
import com.codingshuttle.razorpay.razorpay.payment.repository.OrderRepository;
import com.codingshuttle.razorpay.razorpay.payment.repository.PaymentRepository;
import com.codingshuttle.razorpay.razorpay.payment.service.PaymentService;
import com.codingshuttle.razorpay.razorpay.payment.statemachine.PaymentTransitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;
    private final PaymentTransitionService paymentTransitionService;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest request) {
        OrderRecord order= orderRepository.findByIdAndMerchantId(request.orderId(),merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order",request.orderId()));

        if(order.getOrderStatus() != OrderStatus.CREATED && order.getOrderStatus() != OrderStatus.ATTEMPTED) {
            throw new BusinessRuleViolationException("ORDER_NOT_PAYABLE",
                    "Order cannot accept payment in status: "+ order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts()+1);

        Payment payment = Payment.builder()
                .order(order)
                .merchantId(merchantId)
                .amount(order.getAmount())
                .status(PaymentStatus.CREATED)
                .paymentMethod(request.method())
                .methodDetails(request.methodDetails())
                .build();
        payment = paymentRepository.save(payment);

        PaymentRequest paymentRequest = new PaymentRequest(payment.getId(),
                request.orderId(),merchantId,order.getAmount(),request.method(),
                request.methodDetails());

       PaymentResult result = paymentGatewayRouter.initiate(paymentRequest);

       switch (result) {
           case PaymentResult.Pending pending -> payment.setProcessorReference(pending.registrationRef());
           case PaymentResult.Failure failure ->{
               payment.setStatus(PaymentStatus.FAILED);
               payment.setErrorCode(failure.errorCode());
               payment.setErrorDescription(failure.errorDescription());
           }
           case PaymentResult.Success success -> {

           }
       }

payment = paymentRepository.save(payment);
orderRepository.save(order);

       return paymentMapper.toResponse(payment);

    }

    @Override
    public PaymentResponse capture(UUID merchantId, UUID paymentId) {

        Payment payment = paymentRepository.findByIdAndMerchantId(paymentId,merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment",paymentId));

        paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_REQUEST);

        PaymentResult paymentResult = paymentGatewayRouter.capture(payment.getPaymentMethod(),paymentId);

        if(paymentResult instanceof PaymentResult.Success success) {
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
            payment.setCapturedAt(LocalDateTime.now());
            log.info("Payment captured, paymentID: {}", paymentId);
        } else if(paymentResult instanceof  PaymentResult.Failure failure) {
            paymentTransitionService.apply(payment,PaymentEvent.CAPTURE_FAIL);
            payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDescription());
        }

        payment = paymentRepository.save(payment);

        //TODO: send an outbox (kafka event)

        return paymentMapper.toResponse(payment);

    }
}
