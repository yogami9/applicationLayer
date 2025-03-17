package com.banking.application.service.rmi;

import com.banking.application.exception.AccountNotFoundException;
import com.banking.application.model.Account;
import com.banking.application.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implementation of the AccountRegistry for RMI clients.
 */
@Service
public class AccountRegistryImpl extends UnicastRemoteObject implements AccountRegistry {
    
    private static final Logger logger = LogManager.getLogger(AccountRegistryImpl.class);
    
    @Autowired
    private AccountService accountService;
    
    /**
     * Constructor.
     * 
     * @throws RemoteException If a remote error occurs
     */
    public AccountRegistryImpl() throws RemoteException {
        super();
    }
    
    @Override
    public RemoteAccount getAccount(String accountNumber) throws RemoteException {
        logger.info("RMI request to get account: {}", accountNumber);
        
        try {
            Account account = accountService.getAccount(accountNumber);
            return new RemoteAccountImpl(account, accountService);
        } catch (AccountNotFoundException e) {
            logger.warn("Account not found: {}", accountNumber);
            return null;
        } catch (Exception e) {
            logger.error("Error getting account {}: {}", accountNumber, e.getMessage(), e);
            throw new RemoteException("Error getting account", e);
        }
    }
    
    @Override
    public RemoteAccount createAccount(String accountNumber, String accountHolderName, double initialBalance) 
            throws RemoteException {
        logger.info("RMI request to create account: {}", accountNumber);
        
        try {
            Account account = accountService.createAccount(accountNumber, accountHolderName, initialBalance);
            return new RemoteAccountImpl(account, accountService);
        } catch (Exception e) {
            logger.error("Error creating account {}: {}", accountNumber, e.getMessage(), e);
            throw new RemoteException("Error creating account", e);
        }
    }
}
