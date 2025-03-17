package com.banking.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot application class for the Banking Application Service.
 * This serves as the application tier in the three-tier architecture.
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class BankingApplicationService {
    
    public static void main(String[] args) {
        SpringApplication.run(BankingApplicationService.class, args);
    }
}
