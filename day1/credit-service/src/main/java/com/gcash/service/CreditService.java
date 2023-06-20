package com.gcash.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CreditService {

    private List<Account> accounts = new ArrayList<>();

    // Method to create a new account
    public Account createAccount(Double initialBalance) {
        // Generate a random account ID
        String accountId = UUID.randomUUID().toString();

        // Create a new Account object
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(initialBalance);

        return account;
    }

    // Method to retrieve all accounts
    public List<Account> getAllAccounts() {
        return accounts;
    }

    // Method to add balance to a specific account
    public void addBalance(String accountId, Double amount) {
        for (Account account : accounts) {
            if (account.getId().equals(accountId)) {
                // Calculate new balance by adding the given amount
                double newBalance = account.getBalance() + amount;
                account.setBalance(newBalance);
                return;
            }
        }
    }

    // TODO: Add more methods for updating, deleting, or retrieving specific accounts
}
