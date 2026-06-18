package com.codingshuttle.razorpay.razorpay.merchant.service;

import com.codingshuttle.razorpay.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.codingshuttle.razorpay.razorpay.merchant.dto.response.MerchantResponse;


public interface AuthService {
     MerchantResponse signup( MerchantSignupRequest request);
}
