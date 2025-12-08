package com.t.tiny.ledger.controller.dto;

import java.math.BigDecimal;

public record AccountBalanceDTO(
        long accountNumber,
        String fullName,
        BigDecimal balance
) { }