package com.t.tiny.ledger.controller.request;

import java.math.BigDecimal;

public record DepositRequest(
        BigDecimal amount
) { }