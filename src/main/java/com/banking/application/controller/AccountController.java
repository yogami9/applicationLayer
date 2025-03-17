package com.banking.application.controller;

import com.banking.application.exception.InsufficientFundsException;
import com.banking.application.model.Account;
import com.banking.application.model.Transaction;
import com.banking.application.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for account operations.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private static final Logger logger = LogManager.getLogger(AccountController.class);
    
    @Autowired
    private AccountService accountService;
    
    /**
     * Create a new account.
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account accountData) {
        logger.info("REST request to create account: {}", accountData.getAccountNumber());
        
        Account account = accountService.createAccount(
                accountData.getAccountNumber(),
                accountData.getAccountHolderName(),
                accountData.getBalance());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }
    
    /**
     * Get all accounts.
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        logger.info("REST request to get all accounts");
        
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    
    /**
     * Get an account by account number.
     */
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        logger.info("REST request to get account: {}", accountNumber);
        
        Account account = accountService.getAccount(accountNumber);
        return ResponseEntity.ok(account);
    }
    
    /**
     * Deposit money into an account.
     */
    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable String accountNumber,
            @RequestBody Map<String, Double> depositData) {
        
        Double amount = depositData.get("amount");
        if (amount == null || amount <= 0) {
            return ResponseEntity.badRequest().build();
        }
        
        logger.info("REST request to deposit {} to account {}", amount, accountNumber);
        
        Account account = accountService.deposit(accountNumber, amount);
        return ResponseEntity.ok(account);
    }
    
    /**
     * Withdraw money from an account.
     */
    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<?> withdraw(
            @PathVariable String accountNumber,
            @RequestBody Map<String, Double> withdrawData) {
        
        Double amount = withdrawData.get("amount");
        if (amount == null || amount <= 0) {
            return ResponseEntity.badRequest().build();
        }
        
        logger.info("REST request to withdraw {} from account {}", amount, accountNumber);
        
        try {
            Account account = accountService.withdraw(accountNumber, amount);
            return ResponseEntity.ok(account);
        } catch (InsufficientFundsException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Insufficient funds");
            response.put("requestedAmount", e.getRequestedAmount());
            response.put("availableBalance", e.getAvailableBalance());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Transfer money between accounts.
     */
    @PostMapping("/{sourceAccountNumber}/transfer")
    public ResponseEntity<?> transfer(
            @PathVariable String sourceAccountNumber,
            @RequestBody Map<String, Object> transferData) {
        
        String destinationAccountNumber = (String) transferData.get("destinationAccountNumber");
        Double amount = (Double) transferData.get("amount");
        
        if (destinationAccountNumber == null || amount == null || amount <= 0) {
            return ResponseEntity.badRequest().build();
        }
        
        logger.info("REST request to transfer {} from account {} to account {}", 
                amount, sourceAccountNumber, destinationAccountNumber);
        
        try {
            boolean success = accountService.transfer(sourceAccountNumber, destinationAccountNumber, amount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("sourceAccountNumber", sourceAccountNumber);
            response.put("destinationAccountNumber", destinationAccountNumber);
            response.put("amount", amount);
            
            return ResponseEntity.ok(response);
        } catch (InsufficientFundsException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Insufficient funds");
            response.put("requestedAmount", e.getRequestedAmount());
            response.put("availableBalance", e.getAvailableBalance());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Get transaction history for an account.
     */
    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String accountNumber) {
        logger.info("REST request to get transaction history for account {}", accountNumber);
        
        List<Transaction> transactions = accountService.getTransactionHistory(accountNumber);
        return ResponseEntity.ok(transactions);
    }
}
