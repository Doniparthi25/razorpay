package com.codingshuttle.razorpay.razorpay.merchant.api;

import com.codingshuttle.razorpay.razorpay.common.dto.WebhookTarget;

import java.util.List;
import java.util.UUID;

public interface MerchantWebhookApi {

    List<WebhookTarget> getActiveConfigsForEvent(UUID merchantId,String eventType);
}
