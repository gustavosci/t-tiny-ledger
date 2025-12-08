package com.t.tiny.ledger.service;

import com.t.tiny.ledger.controller.dto.AccountDTO;
import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.db.Account;
import com.t.tiny.ledger.db.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DefaultAccountServiceTest {

    @InjectMocks
    private DefaultAccountService service;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void givenValidAccountRequest_whenCreateAccount_thenSuccess() {
        // Given
        CreateAccountRequest request = new CreateAccountRequest("John Doe", LocalDate.now(), "123 Main St");
        Account expectedAccount = new Account(12345L, "John Doe", LocalDate.now(), "123 Main St");

        given(accountRepository.createAccount(anyLong(), eq(request))).willReturn(expectedAccount);

        // When
        AccountDTO result = service.createAccount(request);

        // Then
        assertEquals(expectedAccount.accountNumber(), result.accountNumber());
        assertEquals(expectedAccount.fullName(), result.fullName());
        assertEquals(expectedAccount.dateOfBirth(), result.dateOfBirth());
        assertEquals(expectedAccount.address(), result.address());
    }

    @Test
    void givenExistingAccountNumber_whenGetAccount_thenReturned() {
        // Given
        long accountNumber = 12345L;
        Account expectedAccount = new Account(accountNumber, "John Doe", LocalDate.now(), "123 Main St");

        given(accountRepository.getAccount(accountNumber)).willReturn(expectedAccount);

        // When
        AccountDTO result = service.getAccount(accountNumber);

        // Then
        assertEquals(expectedAccount.accountNumber(), result.accountNumber());
        assertEquals(expectedAccount.fullName(), result.fullName());
        assertEquals(expectedAccount.dateOfBirth(), result.dateOfBirth());
        assertEquals(expectedAccount.address(), result.address());
    }
}