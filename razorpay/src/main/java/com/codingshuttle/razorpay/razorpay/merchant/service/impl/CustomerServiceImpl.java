package com.codingshuttle.razorpay.razorpay.merchant.service.impl;

import com.codingshuttle.razorpay.razorpay.merchant.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    @Override
    public UUID findOrCreate(UUID merchantId, String email, String name, String phone) {
        return null;
    }
}
