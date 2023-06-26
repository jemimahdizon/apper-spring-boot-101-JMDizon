package com.apper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final List<Account> accounts = new ArrayList<>(); // List to store registered accounts

    private final IdGeneratorService idGeneratorService; // Service to generate unique IDs

    @Autowired
    public AccountService(IdGeneratorService idGeneratorService) {
        this.idGeneratorService = idGeneratorService;
    }

    // Creates a new account with the provided details
    public Account create(String firstName, String lastName, String username, String clearPassword) {
        if (isUsernameRegistered(username)) { // Check if username is already registered
            throw new UsernameAlreadyRegisteredException("Username already registered.");
        }

        Account account = new Account(); // Create a new account object

        String id = idGeneratorService.getNextId(); // Generate a unique ID for the account
        System.out.println("Generated id: " + id);

        account.setId(id);
        account.setBalance(1_000.0);

        LocalDateTime now = LocalDateTime.now();
        account.setCreationDate(now);
        account.setLastUpdated(now);

        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setUsername(username);
        account.setClearPassword(clearPassword);
        account.setVerificationCode(idGeneratorService.generateRandomCharacters(6));

        accounts.add(account); // Add the account to the list of registered accounts

        return account; // Return the created account
    }

    // Retrieves the account with the specified account ID
    public Account get(String accountId) {
        Account account = findAccountById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account not found.");
        }
        return account;
    }

    // Retrieves all registered accounts
    public List<Account> getAll() {
        if (accounts.isEmpty()) {
            throw new IllegalStateException("No registered accounts.");
        }
        return new ArrayList<>(accounts);
    }

    // Updates the details of the account with the specified ID
    public void update(String id, String firstName, String lastName, String username, String clearPassword) {
        Account accountToUpdate = findAccountById(id);
        if (accountToUpdate == null) {
            throw new AccountNotFoundException("Account not found.");
        }

        if (username != null && !username.equals(accountToUpdate.getUsername()) && isUsernameRegistered(username)) {
            throw new UsernameAlreadyRegisteredException("Username already registered.");
        }

        if (firstName != null) {
            accountToUpdate.setFirstName(firstName);
        }
        if (lastName != null) {
            accountToUpdate.setLastName(lastName);
        }
        if (username != null) {
            accountToUpdate.setUsername(username);
        }
        if (clearPassword != null) {
            accountToUpdate.setClearPassword(clearPassword);
        }

        accountToUpdate.setLastUpdated(LocalDateTime.now());
    }

    // Deletes the account with the specified ID
    public void delete(String id) {
        Account accountToDelete = get(id);
        accounts.remove(accountToDelete);
    }

    // Helper function to find an account in the registered accounts list by the account ID
    private Account findAccountById(String accountId) {
        for (Account account : accounts) {
            if (account.getId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }

    // Checks if a username is already registered
    public boolean isUsernameRegistered(String username) {
        for (Account account : accounts) {
            if (account.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
