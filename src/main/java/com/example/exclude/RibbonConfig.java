package com.example.exclude;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.context.annotation.Bean;

import com.example.CustomRestClient;
import com.netflix.client.RetryHandler;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.niws.client.http.RestClient;
import com.netflix.servo.monitor.Monitors;

/**
 * Created by ryanjbaxter on 9/13/16.
 */
public class RibbonConfig {
    @Value("${ribbon.client.name}")
    private String name = "client";

    @Bean
    public RestClient ribbonRestClient(IClientConfig config, ILoadBalancer loadBalancer, ServerIntrospector serverIntrospector, RetryHandler retryHandler) {
        CustomRestClient client = new CustomRestClient(config, serverIntrospector);
        client.setLoadBalancer(loadBalancer);
        client.setRetryHandler(retryHandler);
        Monitors.registerObject("Client_" + name, client);
        return client;
    }
}
