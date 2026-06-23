package com.codingshuttle.razorpay.razorpay.payment.mapper;

import com.codingshuttle.razorpay.razorpay.payment.dto.response.OrderResponse;
import com.codingshuttle.razorpay.razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toResponse(OrderRecord orderRecord);
}
