package com.codingshuttle.razorpay.razorpay.merchant.service.impl;

import com.codingshuttle.razorpay.razorpay.common.enums.MerchantStatus;
import com.codingshuttle.razorpay.razorpay.common.enums.UserRole;
import com.codingshuttle.razorpay.razorpay.common.exceptions.DuplicateResourceException;
import com.codingshuttle.razorpay.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.codingshuttle.razorpay.razorpay.merchant.dto.response.MerchantResponse;
import com.codingshuttle.razorpay.razorpay.merchant.entity.AppUser;
import com.codingshuttle.razorpay.razorpay.merchant.entity.Merchant;
import com.codingshuttle.razorpay.razorpay.merchant.mapper.MerchantMapper;
import com.codingshuttle.razorpay.razorpay.merchant.repository.AppUserRepository;
import com.codingshuttle.razorpay.razorpay.merchant.repository.MerchantRepository;
import com.codingshuttle.razorpay.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRepository appUserRepository;
    private final MerchantMapper merchantMapper;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {
        if (merchantRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL", "merchant with email already exists: " + request.email());
        }

        Merchant merchant = merchantMapper.toEntityFromSignUpRequest(request);
        merchant.setStatus(MerchantStatus.PENDING_KYC);
        merchant = merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(request.password()) // TODO: encrypt using Bcrypt
                .role(UserRole.OWNER)
                .build();
        appUserRepository.save(appUser);

        return merchantMapper.toResponse(merchant);


    }
}
