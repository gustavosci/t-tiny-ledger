package com.t.tiny.ledger.mapper;

import com.t.tiny.ledger.controller.dto.AccountBalanceDTO;
import com.t.tiny.ledger.controller.dto.AccountBaseInfoDTO;
import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.model.Account;

import java.math.RoundingMode;

public final class AccountMapper {

    private AccountMapper() {}

    public static AccountBaseInfoDTO toBaseInfoDTO(Account account) {
        return new AccountBaseInfoDTO(
                account.getAccountNumber(),
                account.getFullName(),
                account.getDateOfBirth(),
                account.getAddress()
        );
    }

    public static AccountBalanceDTO toBalanceDTO(Account account) {
        return new AccountBalanceDTO(
                account.getAccountNumber(),
                account.getFullName(),
                account.getBalance().setScale(2, RoundingMode.HALF_UP)
        );
    }

    public static Account fromCreateAccountRequest(long accountNumber, CreateAccountRequest request) {
        return new Account(
                accountNumber,
                request.fullName(),
                request.dateOfBirth(),
                request.address()
        );
    }
}