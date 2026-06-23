package com.codingshuttle.razorpay.razorpay.merchant.service.impl;

import com.codingshuttle.razorpay.razorpay.common.exceptions.ResourceNotFoundException;
import com.codingshuttle.razorpay.razorpay.common.util.RandomizerUtil;
import com.codingshuttle.razorpay.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.codingshuttle.razorpay.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.codingshuttle.razorpay.razorpay.merchant.dto.response.ApiKeyResponse;
import com.codingshuttle.razorpay.razorpay.merchant.entity.ApiKey;
import com.codingshuttle.razorpay.razorpay.merchant.entity.Merchant;
import com.codingshuttle.razorpay.razorpay.merchant.mapper.ApiKeyMapper;
import com.codingshuttle.razorpay.razorpay.merchant.mapper.MerchantMapper;
import com.codingshuttle.razorpay.razorpay.merchant.repository.ApiKeyRepository;
import com.codingshuttle.razorpay.razorpay.merchant.repository.MerchantRepository;
import com.codingshuttle.razorpay.razorpay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;


    @Override
    @Transactional
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("merchant", merchantId));

        String keyid = "rzp_"+request.environment().name().toLowerCase()+"_"+ RandomizerUtil.randomBase64(24);
        String rawSecret = RandomizerUtil.randomBase64(40);

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyid)
                .keySecretHash(rawSecret) //TODO encode with BcryptPasswordEncoder
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(),keyid,rawSecret,request.environment());
    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {
        return apiKeyMapper.toResponseList(apiKeyRepository.findByMerchant_id(merchantId));
    }

    @Override
    @Transactional
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(()-> new ResourceNotFoundException("ApiKey",keyId));

        apiKey.setEnabled(false);
    }

    @Override
    @Transactional
    public @Nullable ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(()-> new ResourceNotFoundException("ApiKey",keyId));

        if (!apiKey.isEnabled()) throw new RuntimeException("Cannot rotate disabled key");
        String newRawSecret = RandomizerUtil.randomBase64(40);
        apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());
        apiKey.setKeySecretHash(newRawSecret); //TODO: encode with BcryptPasswordEncoder
        apiKey.setRotatedAt(LocalDateTime.now());
        apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));
        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(),apiKey.getKeyId(),
                newRawSecret,apiKey.getEnvironment());
    }
}
