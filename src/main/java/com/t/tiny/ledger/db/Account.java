package com.t.tiny.ledger.db;

import java.time.LocalDate;

public record Account(
        long accountNumber,
        String fullName,
        LocalDate dateOfBirth,
        String address
) { }
