package com.t.tiny.ledger.controller;

import com.t.tiny.ledger.controller.dto.AccountBalanceDTO;
import com.t.tiny.ledger.controller.dto.TransactionDTO;
import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.controller.dto.AccountBaseInfoDTO;
import com.t.tiny.ledger.controller.request.DepositRequest;
import com.t.tiny.ledger.controller.request.WithdrawRequest;
import com.t.tiny.ledger.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    // TODO HANDLE EXCEPTIONS

    private final AccountService accountService;

    @Autowired
    public AccountsController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountBaseInfoDTO createAccount(@RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/{accountNumber}")
    public AccountBaseInfoDTO getAccountInfo(@PathVariable long accountNumber) {
        return accountService.getAccountInfo(accountNumber);
    }

    @GetMapping("/{accountNumber}/balance")
    public AccountBalanceDTO getAccountBalance(@PathVariable long accountNumber) {
        return accountService.getAccountBalance(accountNumber);
    }

    @PostMapping("/{accountNumber}/deposit")
    public void deposit(@PathVariable long accountNumber,
                        @RequestBody DepositRequest request) {
        accountService.deposit(accountNumber, request);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public void withdraw(@PathVariable long accountNumber,
                         @RequestBody WithdrawRequest request) {
        accountService.withdraw(accountNumber, request);
    }

    @GetMapping("/{accountNumber}/transactions")
    public List<TransactionDTO> getAccountTransactions(@PathVariable long accountNumber) {
        return accountService.getTransactions(accountNumber);
    }
}
