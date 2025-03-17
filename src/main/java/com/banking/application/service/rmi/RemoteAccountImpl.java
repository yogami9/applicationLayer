package com.banking.application.service.rmi;

import com.banking.application.exception.InsufficientFundsException;
import com.banking.application.model.Account;
import com.banking.application.model.Transaction;
import com.banking.application.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the RemoteAccount interface for RMI clients.
 */
public class RemoteAccountImpl extends UnicastRemoteObject implements RemoteAccount {
    
    private static final Logger logger = LogManager.getLogger(RemoteAccountImpl.class);
    
    private final Account account;
    private final AccountService accountService;
    
    /**
     * Constructor.
     * 
     * @param account The account
     * @param accountService The account service
     * @throws RemoteException If a remote error occurs
     */
    public RemoteAccountImpl(Account account, AccountService accountService) throws RemoteException {
        super();
        this.account = account;
        this.accountService = accountService;
    }
    
    @Override
    public String getAccountNumber() throws RemoteException {
        return account.getAccountNumber();
    }
    
    @Override
    public double getBalance() throws RemoteException {
        // Always get the latest balance from the service
        return accountService.getAccount(account.getAccountNumber()).getBalance();
    }
    
    @Override
    public double deposit(double amount) throws RemoteException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        
        Account updatedAccount = accountService.deposit(account.getAccountNumber(), amount);
        return updatedAccount.getBalance();
    }
    
    @Override
    public double withdraw(double amount) throws RemoteException, IllegalArgumentException, InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        Account updatedAccount = accountService.withdraw(account.getAccountNumber(), amount);
        return updatedAccount.getBalance();
    }
    
    @Override
    public boolean transfer(RemoteAccount destinationAccount, double amount) 
            throws RemoteException, IllegalArgumentException, InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        
        return accountService.transfer(
                account.getAccountNumber(),
                destinationAccount.getAccountNumber(),
                amount);
    }
    
    @Override
    public List<Transaction> getTransactionHistory() throws RemoteException {
        return accountService.getTransactionHistory(account.getAccountNumber());
    }
    
    @Override
    public String getAccountHolderName() throws RemoteException {
        return account.getAccountHolderName();
    }
    
    @Override
    public Date getCreationDate() throws RemoteException {
        return account.getCreationDate();
    }
}
