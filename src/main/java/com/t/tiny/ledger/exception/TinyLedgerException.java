package com.t.tiny.ledger.exception;

import org.springframework.http.HttpStatus;

public class TinyLedgerException extends RuntimeException {

    private final HttpStatus httpStatus;

    public TinyLedgerException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
