package com.apper;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAccountResponse createAccount(@RequestBody CreateAccountRequest request) {
        Account account = accountService.create(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword());

        CreateAccountResponse response = new CreateAccountResponse();
        response.setVerificationCode(account.getVerificationCode());

        return response;
    }

    @GetMapping("{accountId}")
    public GetAccountResponse getAccount(@PathVariable String accountId) {
        Account account = accountService.get(accountId);
        if (account == null) {
            // Handle the case where the account is not found
            return null; // or throw an appropriate exception or return an error response
        }
        return mapToGetAccountResponse(account);
    }

    @GetMapping
    public List<GetAccountResponse> getAllAccounts() {
        List<Account> accounts = accountService.getAll();
        if (accounts.isEmpty()) {
            // Handle the case where no registered accounts are found
            return Collections.emptyList(); // or throw an appropriate exception or return an error response
        }
        return accounts.stream()
                .map(this::mapToGetAccountResponse)
                .collect(Collectors.toList());
    }

    @PutMapping("{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public UpdateAccountResponse updateAccount(@PathVariable String accountId, @RequestBody UpdateAccountRequest request) {
        Account account = accountService.get(accountId);
        if (account == null) {
            // Handle the case where the account is not found
            return null; // or throw an appropriate exception or return an error response
        }

        accountService.update(
                accountId,
                request.getFirstName(),
                request.getLastName(),
                request.getUsername(),
                request.getClearPassword());

        UpdateAccountResponse response = new UpdateAccountResponse();
        response.setLastUpdated(LocalDateTime.now());

        return response;
    }

    @DeleteMapping("{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable String accountId) {
        Account account = accountService.get(accountId);
        if (account != null) {
            accountService.delete(accountId);
        }
    }

    private GetAccountResponse mapToGetAccountResponse(Account account) {
        GetAccountResponse response = new GetAccountResponse();
        response.setBalance(account.getBalance());
        response.setFirstName(account.getFirstName());
        response.setLastName(account.getLastName());
        response.setUsername(account.getUsername());
        response.setRegistrationDate(account.getCreationDate());
        response.setAccountId(account.getId());

        return response;
    }
}
