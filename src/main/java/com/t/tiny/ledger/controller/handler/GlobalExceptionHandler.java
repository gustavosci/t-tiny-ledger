package com.t.tiny.ledger.controller.handler;

import com.t.tiny.ledger.controller.dto.ApiErrorDTO;
import com.t.tiny.ledger.exception.TinyLedgerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TinyLedgerException.class)
    public ResponseEntity<ApiErrorDTO> handleTinyLedgerException(TinyLedgerException ex) {
        ApiErrorDTO errorResponse = new ApiErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }
}