package com.t.tiny.ledger.db;

import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.exception.AccountAlreadyExistsException;
import com.t.tiny.ledger.mapper.AccountMapper;
import org.springframework.stereotype.Repository;

import com.t.tiny.ledger.exception.AccountNotFountException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountRepository {

    private final Map<Long, Account> accountMap = new ConcurrentHashMap<>();

    // TODO: WHEN TO USE METHOD OR BLOCK SYNCHRONIZED?
    public synchronized Account createAccount(long accountNumber, CreateAccountRequest createAccountRequest) {
        if (accountMap.containsKey(accountNumber)) {
            throw new AccountAlreadyExistsException(accountNumber);
        }

        Account account = AccountMapper.fromRequest(accountNumber, createAccountRequest);
        accountMap.put(accountNumber, account);

        return account;
    }

    public Account getAccount(long accountNumber) {
        return Optional.ofNullable(accountMap.get(accountNumber))
                .orElseThrow(() -> new AccountNotFountException(accountNumber));
    }
}
