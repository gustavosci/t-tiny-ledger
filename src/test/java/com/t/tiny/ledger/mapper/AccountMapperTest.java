package com.t.tiny.ledger.mapper;

import com.t.tiny.ledger.controller.dto.AccountBalanceDTO;
import com.t.tiny.ledger.controller.dto.AccountBaseInfoDTO;
import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AccountMapperTest {

    @Test
    void shouldMapAccountToAccountBaseInfoDTO() {
        // Given
        var account = new Account(123L, "John Doe", LocalDate.of(1990, 1, 1), "123 Main St");
        account.setBalance(BigDecimal.TEN);

        // When
        AccountBaseInfoDTO result = AccountMapper.toBaseInfoDTO(account);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.accountNumber()).isEqualTo(123L);
        assertThat(result.fullName()).isEqualTo("John Doe");
        assertThat(result.dateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(result.address()).isEqualTo("123 Main St");
    }

    @Test
    void shouldMapAccountToAccountBalanceDTO() {
        // Given
        var account = new Account(123L, "John Doe", LocalDate.of(1990, 1, 1), "123 Main St");
        account.setBalance(new BigDecimal("123.456"));

        // When
        AccountBalanceDTO result = AccountMapper.toBalanceDTO(account);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.accountNumber()).isEqualTo(123L);
        assertThat(result.fullName()).isEqualTo("John Doe");
        assertThat(result.balance()).isEqualTo(new BigDecimal("123.46"));
        assertThat(result.balance().scale()).isEqualTo(2);
    }

    @Test
    void shouldMapCreateAccountRequestToAccount() {
        // Given
        long accountNumber = 456L;
        var request = new CreateAccountRequest("Jane Doe", LocalDate.of(1985, 5, 15), "456 Oak Ave");

        // When
        Account result = AccountMapper.fromCreateAccountRequest(accountNumber, request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccountNumber()).isEqualTo(accountNumber);
        assertThat(result.getFullName()).isEqualTo("Jane Doe");
        assertThat(result.getDateOfBirth()).isEqualTo(LocalDate.of(1985, 5, 15));
        assertThat(result.getAddress()).isEqualTo("456 Oak Ave");
        assertThat(result.getBalance()).isEqualTo(BigDecimal.ZERO);
    }
}