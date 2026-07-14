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
                .method(request.method())
                .idempotencyKey(UUID.randomUUID().toString())
                .methodDetails(request.methodDetails())
                .build();
        payment = paymentRepository.save(payment);

        PaymentRequest paymentRequest = new PaymentRequest(payment.getId(),
                request.orderId(),merchantId,order.getAmount(),request.method(),
                request.methodDetails());

        paymentTransitionService.apply(payment,PaymentEvent.AUTHORIZE_ATTEMPT);
       PaymentResult result = paymentGatewayRouter.initiate(paymentRequest);

       switch (result) {
           case PaymentResult.Pending pending -> payment.setProcessorReference(pending.registrationRef());
           case PaymentResult.Failure failure ->{
//               payment.setStatus(PaymentStatus.FAILED);
               paymentTransitionService.apply(payment,PaymentEvent.AUTHORIZE_ATTEMPT);
               payment.setErrorCode(failure.errorCode());
               payment.setErrorDescription(failure.errorDescription());
           }
           case PaymentResult.Success success -> {
               log.warn("Invalid state");
               return null;
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

        PaymentResult paymentResult = paymentGatewayRouter.capture(payment.getMethod(),paymentId);

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

    @Override
    @Transactional
    public void resolveAuthorization(UUID paymentId, boolean approve, String bankRef, String errorCode, String errorDescription) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment", paymentId));

        if (payment.getStatus() != PaymentStatus.AUTHORIZING) {
            log.warn("Payment is not Authorizing state, paymentId: {}, status: {}", paymentId, payment.getStatus());
            return;
        }

        OrderRecord orderRecord = payment.getOrder();

        if (approve) {
            paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_SUCCESS);
            payment.setBankReference(bankRef);
            payment.setAuthorizedAt(LocalDateTime.now());

            //Auto-capture
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_REQUEST);
            PaymentResult captureResult = paymentGatewayRouter.capture(payment.getMethod(), paymentId);

            if (captureResult instanceof PaymentResult.Success success) {
                paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
                payment.setCapturedAt(LocalDateTime.now());
                orderRecord.setOrderStatus(OrderStatus.PAID);
            } else if (captureResult instanceof PaymentResult.Failure failure) {
                paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_FAIL);
                payment.setErrorCode(failure.errorCode());
                payment.setErrorDescription(failure.errorDescription());
            }

        }else {
                paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
                payment.setErrorCode(errorCode);
                payment.setErrorDescription(errorDescription);
            }

            paymentRepository.save(payment);
            orderRepository.save(orderRecord);

        // TODO: send an outbox (kafka event)
    }
}
