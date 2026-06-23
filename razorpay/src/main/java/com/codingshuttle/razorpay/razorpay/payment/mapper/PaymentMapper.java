package com.codingshuttle.razorpay.razorpay.payment.mapper;
import com.codingshuttle.razorpay.razorpay.payment.dto.response.PaymentResponse;
import com.codingshuttle.razorpay.razorpay.payment.entity.OrderRecord;
import com.codingshuttle.razorpay.razorpay.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.lang.annotation.Target;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    @Mapping(target="orderId", source = "order.id")
    PaymentResponse toResponse(Payment payment);

    @Mapping(target="orderId", source="order.id")
    List<PaymentResponse> toResponseList(List<Payment> paymentList);
}