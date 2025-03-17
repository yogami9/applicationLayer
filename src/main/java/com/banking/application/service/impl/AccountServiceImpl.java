package com.banking.application.service.impl;

import com.banking.application.exception.AccountNotFoundException;
import com.banking.application.exception.InsufficientFundsException;
import com.banking.application.model.Account;
import com.banking.application.model.Transaction;
import com.banking.application.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of the AccountService that communicates with the database tier.
 */
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LogManager.getLogger(AccountServiceImpl.class);
    
    @Autowired
    private WebClient webClient;
    
    @Override
    public Account createAccount(String accountNumber, String accountHolderName, double initialBalance) {
        logger.info("Creating account: {} for {}", accountNumber, accountHolderName);
        
        Account account = new Account(accountNumber, accountHolderName, initialBalance);
        
        return webClient.post()
                .uri("/api/accounts")
                .body(Mono.just(account), Account.class)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), 
                        response -> Mono.error(new RuntimeException("Account already exists")))
                .bodyToMono(Account.class)
                .block();
    }
    
    @Override
    @Cacheable(value = "accounts", key = "#accountNumber")
    public Account getAccount(String accountNumber) {
        logger.info("Getting account: {}", accountNumber);
        
        return webClient.get()
                .uri("/api/accounts/{accountNumber}", accountNumber)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), 
                        response -> Mono.error(new AccountNotFoundException(accountNumber)))
                .bodyToMono(Account.class)
                .block();
    }
    
    @Override
    @Cacheable(value = "accounts", key = "'all'")
    public List<Account> getAllAccounts() {
        logger.info("Getting all accounts");
        
        Account[] accounts = webClient.get()
                .uri("/api/accounts")
                .retrieve()
                .bodyToMono(Account[].class)
                .block();
        
        return Arrays.asList(accounts != null ? accounts : new Account[0]);
    }
    
    @Override
    @CacheEvict(value = "accounts", key = "#accountNumber")
    public Account deposit(String accountNumber, double amount) {
        logger.info("Depositing {} to account {}", amount, accountNumber);
        
        // First, get the current account
        Account account = getAccount(accountNumber);
        double newBalance = account.getBalance() + amount;
        
        // Update the balance in the database
        updateBalance(accountNumber, newBalance);
        
        // Record the transaction
        recordTransaction(accountNumber, "DEPOSIT", amount, newBalance, 
                "Deposit", null, accountNumber);
        
        // Update the account object
        account.setBalance(newBalance);
        return account;
    }
    
    @Override
    @CacheEvict(value = "accounts", key = "#accountNumber")
    public Account withdraw(String accountNumber, double amount) throws InsufficientFundsException {
        logger.info("Withdrawing {} from account {}", amount, accountNumber);
        
        // First, get the current account
        Account account = getAccount(accountNumber);
        
        // Check if there are sufficient funds
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException(amount, account.getBalance());
        }
        
        double newBalance = account.getBalance() - amount;
        
        // Update the balance in the database
        updateBalance(accountNumber, newBalance);
        
        // Record the transaction
        recordTransaction(accountNumber, "WITHDRAWAL", amount, newBalance, 
                "Withdrawal", accountNumber, null);
        
        // Update the account object
        account.setBalance(newBalance);
        return account;
    }
    
    @Override
    public boolean transfer(String sourceAccountNumber, String destinationAccountNumber, double amount) 
            throws InsufficientFundsException {
        logger.info("Transferring {} from account {} to account {}", 
                amount, sourceAccountNumber, destinationAccountNumber);
        
        // First, get the source account
        Account sourceAccount = getAccount(sourceAccountNumber);
        
        // Check if the destination account exists
        Account destinationAccount = getAccount(destinationAccountNumber);
        
        // Check if there are sufficient funds
        if (sourceAccount.getBalance() < amount) {
            throw new InsufficientFundsException(amount, sourceAccount.getBalance());
        }
        
        // Update source account
        double sourceNewBalance = sourceAccount.getBalance() - amount;
        updateBalance(sourceAccountNumber, sourceNewBalance);
        
        // Record outgoing transaction
        recordTransaction(sourceAccountNumber, "TRANSFER_OUT", amount, sourceNewBalance, 
                "Transfer to account " + destinationAccountNumber, 
                sourceAccountNumber, destinationAccountNumber);
        
        // Update destination account
        double destNewBalance = destinationAccount.getBalance() + amount;
        updateBalance(destinationAccountNumber, destNewBalance);
        
        // Record incoming transaction
        recordTransaction(destinationAccountNumber, "TRANSFER_IN", amount, destNewBalance, 
                "Transfer from account " + sourceAccountNumber, 
                sourceAccountNumber, destinationAccountNumber);
        
        return true;
    }
    
    @Override
    public List<Transaction> getTransactionHistory(String accountNumber) {
        logger.info("Getting transaction history for account {}", accountNumber);
        
        Transaction[] transactions = webClient.get()
                .uri("/api/transactions/account/{accountNumber}", accountNumber)
                .retrieve()
                .bodyToMono(Transaction[].class)
                .block();
        
        return Arrays.asList(transactions != null ? transactions : new Transaction[0]);
    }
    
    /**
     * Update an account's balance in the database.
     * 
     * @param accountNumber Account number
     * @param newBalance New balance
     */
    @CacheEvict(value = "accounts", allEntries = true)
    private void updateBalance(String accountNumber, double newBalance) {
        logger.info("Updating balance for account {}: new balance = {}", accountNumber, newBalance);
        
        Map<String, Double> balanceMap = new HashMap<>();
        balanceMap.put("balance", newBalance);
        
        webClient.put()
                .uri("/api/accounts/{accountNumber}/balance", accountNumber)
                .body(Mono.just(balanceMap), Map.class)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), 
                        response -> Mono.error(new AccountNotFoundException(accountNumber)))
                .bodyToMono(Map.class)
                .block();
    }
    
    /**
     * Record a transaction in the database.
     * 
     * @param accountNumber Account number
     * @param transactionType Transaction type
     * @param amount Transaction amount
     * @param resultingBalance Resulting balance
     * @param description Description
     * @param sourceAccount Source account
     * @param destinationAccount Destination account
     */
    private void recordTransaction(String accountNumber, String transactionType, double amount, 
                                  double resultingBalance, String description, 
                                  String sourceAccount, String destinationAccount) {
        logger.info("Recording transaction: account={}, type={}, amount={}", 
                accountNumber, transactionType, amount);
        
        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("transactionId", UUID.randomUUID().toString());
        transactionData.put("accountNumber", accountNumber);
        transactionData.put("transactionType", transactionType);
        transactionData.put("amount", amount);
        transactionData.put("resultingBalance", resultingBalance);
        transactionData.put("description", description);
        transactionData.put("sourceAccount", sourceAccount);
        transactionData.put("destinationAccount", destinationAccount);
        
        webClient.post()
                .uri("/api/transactions")
                .body(Mono.just(transactionData), Map.class)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
