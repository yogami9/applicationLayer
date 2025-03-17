package com.banking.application.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Transaction model that mirrors the one from the database tier.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    }
    
    private String transactionId;
    private Date timestamp;
    private String type;
    private double amount;
    private double resultingBalance;
    private String description;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    
    // Default constructor for JSON deserialization
    public Transaction() {
    }
    
    public Transaction(String transactionId, String type, double amount, 
                       double resultingBalance, String description,
                       String sourceAccountNumber, String destinationAccountNumber) {
        this.transactionId = transactionId;
        this.timestamp = new Date();
        this.type = type;
        this.amount = amount;
        this.resultingBalance = resultingBalance;
        this.description = description;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
    }
    
    // Getters and setters
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public double getResultingBalance() {
        return resultingBalance;
    }
    
    public void setResultingBalance(double resultingBalance) {
        this.resultingBalance = resultingBalance;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }
    
    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }
    
    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }
    
    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", resultingBalance=" + resultingBalance +
                ", description='" + description + '\'' +
                '}';
    }
}
