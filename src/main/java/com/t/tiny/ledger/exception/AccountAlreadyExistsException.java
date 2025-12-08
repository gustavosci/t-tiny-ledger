package com.t.tiny.ledger.exception;

import org.springframework.http.HttpStatus;

public class AccountAlreadyExistsException extends TinyLedgerException {

    public AccountAlreadyExistsException(long accountNumber) {
        super(HttpStatus.CONFLICT, String.format("Account number %d already exists.", accountNumber));
    }
}
