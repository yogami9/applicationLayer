package com.banking.application.config;

import com.banking.application.service.rmi.AccountRegistryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

/**
 * Configuration class for exposing services via RMI for backward compatibility
 * with existing client applications.
 */
@Configuration
public class RmiServerConfig {

    @Value("${rmi.port:1099}")
    private int rmiPort;

    @Value("${rmi.account.prefix:Account/}")
    private String accountPrefix;

    @Bean
    public RmiServiceExporter accountRegistryService(AccountRegistryImpl accountRegistry) {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("AccountRegistry");
        exporter.setService(accountRegistry);
        exporter.setServiceInterface(AccountRegistryImpl.class);
        exporter.setRegistryPort(rmiPort);
        return exporter;
    }
}
