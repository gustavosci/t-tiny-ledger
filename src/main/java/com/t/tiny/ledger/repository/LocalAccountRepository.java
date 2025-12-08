package com.t.tiny.ledger.repository;

import com.t.tiny.ledger.exception.AccountAlreadyExistsException;
import com.t.tiny.ledger.model.Account;
import org.springframework.stereotype.Repository;

import com.t.tiny.ledger.exception.AccountNotFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LocalAccountRepository implements AccountRepository {

    private final Map<Long, Account> accountMap = new ConcurrentHashMap<>();

    // TODO: WHEN TO USE METHOD OR BLOCK SYNCHRONIZED?
    @Override
    public synchronized void createAccount(Account account) {
        if (accountMap.containsKey(account.getAccountNumber())) {
            throw new AccountAlreadyExistsException(account.getAccountNumber());
        }

        accountMap.put(account.getAccountNumber(), account);
    }

    @Override
    public Account getAccount(long accountNumber) {
        return Optional.ofNullable(accountMap.get(accountNumber))
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    @Override
    public void updateAccount(Account account) {
        accountMap.put(account.getAccountNumber(), account);
    }
}
