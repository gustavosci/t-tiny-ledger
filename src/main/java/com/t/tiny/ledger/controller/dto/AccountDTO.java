package com.t.tiny.ledger.controller.dto;

import java.time.LocalDate;

public record AccountDTO(
        long accountNumber,
        String fullName,
        LocalDate dateOfBirth,
        String address
) { }