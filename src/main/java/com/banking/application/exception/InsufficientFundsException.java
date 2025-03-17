package com.banking.application.exception;

import java.io.Serializable;

/**
 * Exception thrown when a withdrawal or transfer is attempted with insufficient funds.
 */
public class InsufficientFundsException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    private final double requestedAmount;
    private final double availableBalance;
    
    /**
     * Constructor for the InsufficientFundsException.
     * 
     * @param requestedAmount The amount requested for withdrawal or transfer
     * @param availableBalance The available balance in the account
     */
    public InsufficientFundsException(double requestedAmount, double availableBalance) {
        super(String.format("Insufficient funds: Requested %.2f but only %.2f available", 
                requestedAmount, availableBalance));
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }
    
    /**
     * Get the amount requested for withdrawal or transfer.
     * 
     * @return The requested amount
     */
    public double getRequestedAmount() {
        return requestedAmount;
    }
    
    /**
     * Get the available balance in the account.
     * 
     * @return The available balance
     */
    public double getAvailableBalance() {
        return availableBalance;
    }
}
