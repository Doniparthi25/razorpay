package com.codingshuttle.razorpay.razorpay.merchant.mapper;

import ch.qos.logback.core.model.ComponentModel;
import com.codingshuttle.razorpay.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.codingshuttle.razorpay.razorpay.merchant.dto.response.MerchantResponse;
import com.codingshuttle.razorpay.razorpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {

    Merchant toEntityFromSignUpRequest(MerchantSignupRequest request);

    MerchantResponse toResponse(Merchant merchant);
}
