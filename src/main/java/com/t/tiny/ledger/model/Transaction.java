package com.t.tiny.ledger.model;

import java.math.BigDecimal;

public record Transaction(
        long accountNumber,
        long timestamp,
        Operation operation,
        BigDecimal amount
) {
    public enum Operation {DEPOSIT, WITHDRAW}
}
