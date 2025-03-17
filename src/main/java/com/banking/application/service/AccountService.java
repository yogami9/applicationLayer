package com.banking.application.service;

import com.banking.application.exception.InsufficientFundsException;
import com.banking.application.model.Account;
import com.banking.application.model.Transaction;

import java.util.List;

/**
 * Service interface for account operations.
 */
public interface AccountService {
    
    /**
     * Create a new account.
     * 
     * @param accountNumber Account number
     * @param accountHolderName Account holder name
     * @param initialBalance Initial balance
     * @return The created account
     */
    Account createAccount(String accountNumber, String accountHolderName, double initialBalance);
    
    /**
     * Get an account by its account number.
     * 
     * @param accountNumber Account number
     * @return The account
     */
    Account getAccount(String accountNumber);
    
    /**
     * Get all accounts.
     * 
     * @return List of all accounts
     */
    List<Account> getAllAccounts();
    
    /**
     * Deposit money into an account.
     * 
     * @param accountNumber Account number
     * @param amount Amount to deposit
     * @return The updated account
     */
    Account deposit(String accountNumber, double amount);
    
    /**
     * Withdraw money from an account.
     * 
     * @param accountNumber Account number
     * @param amount Amount to withdraw
     * @return The updated account
     * @throws InsufficientFundsException If there are insufficient funds
     */
    Account withdraw(String accountNumber, double amount) throws InsufficientFundsException;
    
    /**
     * Transfer money between accounts.
     * 
     * @param sourceAccountNumber Source account number
     * @param destinationAccountNumber Destination account number
     * @param amount Amount to transfer
     * @return True if the transfer was successful
     * @throws InsufficientFundsException If there are insufficient funds
     */
    boolean transfer(String sourceAccountNumber, String destinationAccountNumber, double amount) 
            throws InsufficientFundsException;
    
    /**
     * Get the transaction history for an account.
     * 
     * @param accountNumber Account number
     * @return List of transactions
     */
    List<Transaction> getTransactionHistory(String accountNumber);
}
