package com.example;

import com.netflix.client.RequestSpecificRetryHandler;
import com.netflix.client.RetryHandler;
import com.netflix.client.config.IClientConfig;

public class CustomRetryHandler extends RequestSpecificRetryHandler {
    public CustomRetryHandler(boolean okToRetryOnConnectErrors, boolean okToRetryOnAllErrors) {
        super(okToRetryOnConnectErrors, okToRetryOnAllErrors);
    }

    public CustomRetryHandler(boolean okToRetryOnConnectErrors, boolean okToRetryOnAllErrors, RetryHandler baseRetryHandler, IClientConfig requestConfig) {
        super(okToRetryOnConnectErrors, okToRetryOnAllErrors, baseRetryHandler, requestConfig);
    }

    @Override
    public boolean isRetriableException(Throwable e, boolean sameServer) {
        if(e instanceof ZuulRetryerApplication.ServiceException) {
            return true;
        }
        return super.isRetriableException(e, sameServer);
    }
}
