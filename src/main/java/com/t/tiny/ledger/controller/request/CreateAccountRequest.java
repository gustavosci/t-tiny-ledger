package com.t.tiny.ledger.controller.request;

import java.time.LocalDate;

public record CreateAccountRequest(
        String fullName,
        LocalDate dateOfBirth,
        String address
) { }