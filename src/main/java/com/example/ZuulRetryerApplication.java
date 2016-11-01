package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import com.example.exclude.RibbonConfig;

@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableZuulProxy
@RibbonClients(defaultConfiguration = RibbonConfig.class)
@ComponentScan(basePackages = "com.example",
		excludeFilters =@ComponentScan.Filter(
				type = FilterType.REGEX,
				pattern = "com.example.exclude.*"))
public class ZuulRetryerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulRetryerApplication.class, args);
	}

	static class ServiceException extends RuntimeException {
		public ServiceException(String reason) {
			super(reason);
		}
	}
}
