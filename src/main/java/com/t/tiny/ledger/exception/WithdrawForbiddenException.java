package com.t.tiny.ledger.exception;

import org.springframework.http.HttpStatus;

public class WithdrawForbiddenException extends TinyLedgerException {

    public WithdrawForbiddenException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
