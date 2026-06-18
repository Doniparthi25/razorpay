package com.codingshuttle.razorpay.razorpay.merchant.service.impl;

import com.codingshuttle.razorpay.razorpay.common.exceptions.ResourceNotFoundException;
import com.codingshuttle.razorpay.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.codingshuttle.razorpay.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.codingshuttle.razorpay.razorpay.merchant.entity.ApiKey;
import com.codingshuttle.razorpay.razorpay.merchant.entity.Merchant;
import com.codingshuttle.razorpay.razorpay.merchant.repository.ApiKeyRepository;
import com.codingshuttle.razorpay.razorpay.merchant.repository.MerchantRepository;
import com.codingshuttle.razorpay.razorpay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("merchant", merchantId));

        String keyid = "rzp_"+request.environment().name().toUpperCase()+"big_random_string";
        String rawSecret = "big_random_secret"; //TODO: replace with cryptographic random hex

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyid)
                .keySecretHash(rawSecret) //TODO encode with BcryptPasswordEncoder
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(),keyid,rawSecret,request.environment());
    }
}
