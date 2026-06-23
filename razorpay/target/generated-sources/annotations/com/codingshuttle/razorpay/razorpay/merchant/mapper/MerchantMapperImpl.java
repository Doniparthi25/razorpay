package com.codingshuttle.razorpay.razorpay.merchant.mapper;

import com.codingshuttle.razorpay.razorpay.common.enums.BusinessType;
import com.codingshuttle.razorpay.razorpay.common.enums.MerchantStatus;
import com.codingshuttle.razorpay.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.codingshuttle.razorpay.razorpay.merchant.dto.response.MerchantResponse;
import com.codingshuttle.razorpay.razorpay.merchant.entity.Merchant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-23T10:27:17+0530",
    comments = "version: 1.6.0, compiler: javac, environment: Java 26.0.1 (Oracle Corporation)"
)
@Component
public class MerchantMapperImpl implements MerchantMapper {

    @Override
    public Merchant toEntityFromSignUpRequest(MerchantSignupRequest request) {
        if ( request == null ) {
            return null;
        }

        Merchant.MerchantBuilder merchant = Merchant.builder();

        merchant.name( request.name() );
        merchant.email( request.email() );
        merchant.businessType( request.businessType() );
        merchant.businessName( request.businessName() );

        return merchant.build();
    }

    @Override
    public MerchantResponse toResponse(Merchant merchant) {
        if ( merchant == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String email = null;
        String businessName = null;
        BusinessType businessType = null;

        id = merchant.getId();
        name = merchant.getName();
        email = merchant.getEmail();
        businessName = merchant.getBusinessName();
        businessType = merchant.getBusinessType();

        MerchantStatus merchantStatus = null;

        MerchantResponse merchantResponse = new MerchantResponse( id, name, email, businessName, businessType, merchantStatus );

        return merchantResponse;
    }
}
