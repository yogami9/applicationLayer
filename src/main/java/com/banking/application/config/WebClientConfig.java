package com.banking.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for WebClient used to make HTTP requests to the database tier.
 */
@Configuration
public class WebClientConfig {

    @Value("${database.tier.url}")
    private String databaseTierUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(databaseTierUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
