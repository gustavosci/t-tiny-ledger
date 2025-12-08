package com.t.tiny.ledger.controller.dto;

import java.time.LocalDate;

public record AccountBaseInfoDTO(
        long accountNumber,
        String fullName,
        LocalDate dateOfBirth,
        String address
) { }