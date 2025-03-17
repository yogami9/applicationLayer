package com.banking.application.service.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for retrieving accounts.
 * This allows existing RMI clients to connect to this service.
 */
public interface AccountRegistry extends Remote {
    
    /**
     * Get an account by account number.
     * 
     * @param accountNumber The account number
     * @return The account, or null if not found
     * @throws RemoteException If a remote error occurs
     */
    RemoteAccount getAccount(String accountNumber) throws RemoteException;
    
    /**
     * Create a new account.
     * 
     * @param accountNumber The account number
     * @param accountHolderName The account holder name
     * @param initialBalance The initial balance
     * @return The created account, or null if creation failed
     * @throws RemoteException If a remote error occurs
     */
    RemoteAccount createAccount(String accountNumber, String accountHolderName, double initialBalance) 
            throws RemoteException;
}
