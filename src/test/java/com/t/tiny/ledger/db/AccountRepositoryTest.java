package com.t.tiny.ledger.db;

import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.exception.AccountAlreadyExistsException;
import com.t.tiny.ledger.exception.AccountNotFountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryTest {

    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository = new AccountRepository();
    }

    @Test
    void givenValidAccountRequest_whenCreateAccount_thenSuccess() {
        // Given
        long accountNumber = 12345L;
        CreateAccountRequest request = new CreateAccountRequest("John Doe", LocalDate.now(), "123 Main St");

        // When
        Account result = accountRepository.createAccount(accountNumber, request);

        // Then
        assertEquals(accountNumber, result.accountNumber());
        assertEquals(request.fullName(), result.fullName());
        assertEquals(request.dateOfBirth(), result.dateOfBirth());
        assertEquals(request.address(), result.address());
    }

    @Test
    void givenAccountAlreadyExists_whenCreateAccount_thenException() {
        // Given
        long accountNumber = 12345L;
        CreateAccountRequest request = new CreateAccountRequest("John Doe", LocalDate.now(), "123 Main St");
        accountRepository.createAccount(accountNumber, request);

        // When/Then
        assertThrows(AccountAlreadyExistsException.class, () -> accountRepository.createAccount(accountNumber, request));
    }

    @Test
    void givenExistingAccountNumber_whenGetAccount_thenReturned() {
        // Given
        long accountNumber = 12345L;
        CreateAccountRequest request = new CreateAccountRequest("John Doe", LocalDate.now(), "123 Main St");
        accountRepository.createAccount(accountNumber, request);

        // When
        Account result = accountRepository.getAccount(accountNumber);

        assertEquals(accountNumber, result.accountNumber());
        assertEquals(request.fullName(), result.fullName());
        assertEquals(request.dateOfBirth(), result.dateOfBirth());
        assertEquals(request.address(), result.address());

    }

    @Test
    void givenNotExistingAccountNumber_whenGetAccount_thenNotFound() {
        long accountNumber = 12345L;
        assertThrows(AccountNotFountException.class, () -> accountRepository.getAccount(accountNumber));
    }
}