package com.t.tiny.ledger.service;

import com.t.tiny.ledger.controller.dto.AccountDTO;
import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.db.Account;
import com.t.tiny.ledger.db.AccountRepository;
import com.t.tiny.ledger.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class DefaultAccountService implements AccountService {

    private static final long MAX_ACCOUNT_NUMBER = 1000000000000000000L;

    private final AccountRepository accountRepository;

    @Autowired
    public DefaultAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDTO createAccount(CreateAccountRequest createAccountRequest) {
        // TODO: RETRY ON CONFLICT
        long accountNumber = ThreadLocalRandom.current().nextLong(1, MAX_ACCOUNT_NUMBER);
        Account account = accountRepository.createAccount(accountNumber, createAccountRequest);
        return AccountMapper.toDTO(account);
    }

    @Override
    public AccountDTO getAccount(long accountNumber) {
        Account account = accountRepository.getAccount(accountNumber);
        return AccountMapper.toDTO(account);
    }
}
