package com.t.tiny.ledger.repository;

import com.t.tiny.ledger.model.Account;

public interface AccountRepository {

    void createAccount(Account account);

    void updateAccount(Account account);

    Account getAccount(long accountNumber);
}
