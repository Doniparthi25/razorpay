package com.codingshuttle.razorpay.razorpay.vault.controller;

import com.codingshuttle.razorpay.razorpay.merchant.security.MerchantContext;
import com.codingshuttle.razorpay.razorpay.vault.dto.request.TokenizeRequest;
import com.codingshuttle.razorpay.razorpay.vault.dto.response.TokenizeResponse;
import com.codingshuttle.razorpay.razorpay.vault.service.VaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/vault")
public class VaultController {

    private final MerchantContext merchantContext;
    private final VaultService valutService;

    @PostMapping
    public ResponseEntity<TokenizeResponse> tokenize(@Valid @RequestBody TokenizeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(valutService.tokenize(request, merchantContext.getMerchantId()));
    }

}
