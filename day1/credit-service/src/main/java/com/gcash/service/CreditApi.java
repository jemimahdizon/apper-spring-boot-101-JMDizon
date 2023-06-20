package com.gcash.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CreditApi {

    private final CreditService creditService;

    // Constructor injection of CreditService
    public CreditApi(CreditService creditService) {
        this.creditService = creditService;
    }

    // POST endpoint to create a new account
    @PostMapping
    public Account createNewAccount(@RequestBody Double initialBalance) {
        return creditService.createAccount(initialBalance);
    }

    // GET endpoint to retrieve all accounts
    @GetMapping
    public List<Account> getAllAccounts() {
        return creditService.getAllAccounts();
    }

    // TODO: Add more API endpoints and business logic as needed
}
