package com.codingshuttle.razorpay.razorpay.merchant.controller;

import com.codingshuttle.razorpay.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.codingshuttle.razorpay.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.codingshuttle.razorpay.razorpay.merchant.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> create(@PathVariable UUID merchantId,
                                                       @Valid @RequestBody CreateApiKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(apiKeyService.create(merchantId,request));
    }
}
