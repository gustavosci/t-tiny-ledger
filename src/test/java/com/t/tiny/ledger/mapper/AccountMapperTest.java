package com.t.tiny.ledger.mapper;

import com.t.tiny.ledger.controller.dto.AccountDTO;
import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.db.Account;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountMapperTest {

    @Test
    void shouldMapAccountToAccountDTO() {
        // Given
        var account = new Account(
                12345L,
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "123 Main St"
        );

        // When
        AccountDTO accountDTO = AccountMapper.toDTO(account);

        // Then
        assertEquals(account.accountNumber(), accountDTO.accountNumber());
        assertEquals(account.fullName(), accountDTO.fullName());
        assertEquals(account.dateOfBirth(), accountDTO.dateOfBirth());
        assertEquals(account.address(), accountDTO.address());
    }

    @Test
    void shouldMapCreateAccountRequestToAccount() {
        // Given
        long accountNumber = 12345L;
        var request = new CreateAccountRequest(
                "Jane Doe",
                LocalDate.of(1992, 2, 2),
                "456 Oak Ave"
        );

        // When
        Account account = AccountMapper.fromRequest(accountNumber, request);

        // Then
        assertEquals(accountNumber, account.accountNumber());
        assertEquals(request.fullName(), account.fullName());
        assertEquals(request.dateOfBirth(), account.dateOfBirth());
        assertEquals(request.address(), account.address());
    }
}