package com.rempms.rempms_notification_service.config.webClient;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author @maleeshasa
 * @Date 2025-10-25
 */
@Configuration
public class WebClientConfig {

    /**
     * WebClient for service discovery (e.g., http://message-service)
     */
    @Bean(name = "serviceNameWebClient")
    @LoadBalanced
    public WebClient serviceNameWebClient(WebClient.Builder builder) {
        return builder.build();
    }

    /**
     * WebClient for direct URL-based calls (e.g., http://localhost:8082)
     */
    @Bean(name = "urlBasedWebClient")
    public WebClient urlBasedWebClient(WebClient.Builder builder) {
        return builder.build();
    }
}
