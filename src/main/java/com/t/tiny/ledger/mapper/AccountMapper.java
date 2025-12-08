package com.t.tiny.ledger.mapper;

import com.t.tiny.ledger.controller.dto.AccountDTO;
import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.db.Account;

public final class AccountMapper {

    private AccountMapper() {}

    public static AccountDTO toDTO(Account account) {
        return new AccountDTO(
                account.accountNumber(),
                account.fullName(),
                account.dateOfBirth(),
                account.address()
        );
    }

    public static Account fromRequest(long accountNumber, CreateAccountRequest request) {
        return new Account(
                accountNumber,
                request.fullName(),
                request.dateOfBirth(),
                request.address()
        );
    }
}