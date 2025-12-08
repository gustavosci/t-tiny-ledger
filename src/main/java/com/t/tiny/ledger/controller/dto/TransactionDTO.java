package com.t.tiny.ledger.controller.dto;

import com.t.tiny.ledger.model.Transaction;

import java.math.BigDecimal;

public record TransactionDTO(
        long accountNumber,
        long timestamp,
        Transaction.Operation operation,
        BigDecimal amount
) { }