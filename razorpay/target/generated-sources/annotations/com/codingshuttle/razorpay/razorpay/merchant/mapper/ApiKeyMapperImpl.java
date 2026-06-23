package com.codingshuttle.razorpay.razorpay.merchant.mapper;

import com.codingshuttle.razorpay.razorpay.common.enums.Environment;
import com.codingshuttle.razorpay.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.codingshuttle.razorpay.razorpay.merchant.dto.response.ApiKeyResponse;
import com.codingshuttle.razorpay.razorpay.merchant.entity.ApiKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-23T10:27:18+0530",
    comments = "version: 1.6.0, compiler: javac, environment: Java 26.0.1 (Oracle Corporation)"
)
@Component
public class ApiKeyMapperImpl implements ApiKeyMapper {

    @Override
    public ApiKeyCreateResponse toCreateResponse(ApiKey apiKey) {
        if ( apiKey == null ) {
            return null;
        }

        UUID id = null;
        String keyId = null;
        Environment environment = null;

        id = apiKey.getId();
        keyId = apiKey.getKeyId();
        environment = apiKey.getEnvironment();

        String keySecret = null;

        ApiKeyCreateResponse apiKeyCreateResponse = new ApiKeyCreateResponse( id, keyId, keySecret, environment );

        return apiKeyCreateResponse;
    }

    @Override
    public List<ApiKeyResponse> toResponseList(List<ApiKey> apiKeyList) {
        if ( apiKeyList == null ) {
            return null;
        }

        List<ApiKeyResponse> list = new ArrayList<ApiKeyResponse>( apiKeyList.size() );
        for ( ApiKey apiKey : apiKeyList ) {
            list.add( apiKeyToApiKeyResponse( apiKey ) );
        }

        return list;
    }

    protected ApiKeyResponse apiKeyToApiKeyResponse(ApiKey apiKey) {
        if ( apiKey == null ) {
            return null;
        }

        UUID id = null;
        String keyId = null;
        Environment environment = null;
        boolean enabled = false;
        LocalDateTime lastUsedAt = null;

        id = apiKey.getId();
        keyId = apiKey.getKeyId();
        environment = apiKey.getEnvironment();
        enabled = apiKey.isEnabled();
        lastUsedAt = apiKey.getLastUsedAt();

        LocalDateTime createdAt = null;

        ApiKeyResponse apiKeyResponse = new ApiKeyResponse( id, keyId, environment, enabled, lastUsedAt, createdAt );

        return apiKeyResponse;
    }
}
