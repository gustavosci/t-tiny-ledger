package com.t.tiny.ledger.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFountException extends TinyLedgerException {

    public AccountNotFountException(long accountNumber) {
        super(HttpStatus.NOT_FOUND, String.format("Account number %d not found", accountNumber));
    }
}
