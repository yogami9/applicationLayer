package com.banking.application.service.rmi;

import com.banking.application.exception.InsufficientFundsException;
import com.banking.application.model.Transaction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 * Remote interface for bank account operations.
 * This interface mirrors the Account interface from the original application.
 */
public interface RemoteAccount extends Remote {
    
    /**
     * Get the account number.
     * 
     * @return The account number
     * @throws RemoteException If a remote communication error occurs
     */
    String getAccountNumber() throws RemoteException;
    
    /**
     * Get the current balance of the account.
     * 
     * @return The current balance
     * @throws RemoteException If a remote communication error occurs
     */
    double getBalance() throws RemoteException;
    
    /**
     * Deposit money into the account.
     * 
     * @param amount The amount to deposit
     * @return The new balance after deposit
     * @throws RemoteException If a remote communication error occurs
     * @throws IllegalArgumentException If the amount is negative or zero
     */
    double deposit(double amount) throws RemoteException, IllegalArgumentException;
    
    /**
     * Withdraw money from the account.
     * 
     * @param amount The amount to withdraw
     * @return The new balance after withdrawal
     * @throws RemoteException If a remote communication error occurs
     * @throws IllegalArgumentException If the amount is negative or zero
     * @throws InsufficientFundsException If there are not enough funds in the account
     */
    double withdraw(double amount) throws RemoteException, IllegalArgumentException, InsufficientFundsException;
    
    /**
     * Transfer money to another account.
     * 
     * @param destinationAccount The account to transfer money to
     * @param amount The amount to transfer
     * @return True if the transfer was successful
     * @throws RemoteException If a remote communication error occurs
     * @throws IllegalArgumentException If the amount is negative or zero
     * @throws InsufficientFundsException If there are not enough funds in the account
     */
    boolean transfer(RemoteAccount destinationAccount, double amount) 
            throws RemoteException, IllegalArgumentException, InsufficientFundsException;
    
    /**
     * Get the transaction history for this account.
     * 
     * @return A list of transactions
     * @throws RemoteException If a remote communication error occurs
     */
    List<Transaction> getTransactionHistory() throws RemoteException;
    
    /**
     * Get the account holder's name.
     * 
     * @return The account holder's name
     * @throws RemoteException If a remote communication error occurs
     */
    String getAccountHolderName() throws RemoteException;
    
    /**
     * Get the date when the account was created.
     * 
     * @return The creation date
     * @throws RemoteException If a remote communication error occurs
     */
    Date getCreationDate() throws RemoteException;
}
