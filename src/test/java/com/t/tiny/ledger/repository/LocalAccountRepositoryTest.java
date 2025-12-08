package com.t.tiny.ledger.repository;

import com.t.tiny.ledger.exception.AccountAlreadyExistsException;
import com.t.tiny.ledger.exception.AccountNotFoundException;
import com.t.tiny.ledger.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LocalAccountRepositoryTest {

    private LocalAccountRepository localAccountRepository;

    @BeforeEach
    void setUp() {
        localAccountRepository = new LocalAccountRepository();
    }

    @Test
    void validateCreateAccount() {
        Account account = new Account(12345L, "Test User", java.time.LocalDate.now(), "Test Address");
        localAccountRepository.createAccount(account);

        assertTrue(localAccountRepository.exists(12345L));
    }

    @Test
    void validateCreateAccountAlreadyExists() {
        Account account = new Account(12345L, "Test User", java.time.LocalDate.now(), "Test Address");
        localAccountRepository.createAccount(account);

        assertThrows(AccountAlreadyExistsException.class, () -> localAccountRepository.createAccount(account));
    }

    @Test
    void validateGetAccount() {
        Account account = new Account(12345L, "Test User", java.time.LocalDate.now(), "Test Address");
        localAccountRepository.createAccount(account);

        Account result = localAccountRepository.getAccount(12345L);

        assertEquals(account, result);
    }

    @Test
    void validateGetAccountNotFound() {
        assertThrows(AccountNotFoundException.class, () -> localAccountRepository.getAccount(12345L));
    }

    @Test
    void validateUpdateAccount() {
        Account account = new Account(12345L, "Test User", java.time.LocalDate.now(), "Test Address");
        localAccountRepository.createAccount(account);

        account.setBalance(BigDecimal.valueOf(2000));
        localAccountRepository.updateAccount(account);

        Account result = localAccountRepository.getAccount(12345L);
        assertEquals(BigDecimal.valueOf(2000), result.getBalance());
    }

    @Test
    void validateUpdateAccountNotFound() {
        Account account = new Account(12345L, "Test User", java.time.LocalDate.now(), "Test Address");
        assertThrows(AccountNotFoundException.class, () -> localAccountRepository.updateAccount(account));
    }

    @Test
    void validateAccountExists() {
        Account account = new Account(12345L, "Test User", java.time.LocalDate.now(), "Test Address");
        localAccountRepository.createAccount(account);

        assertTrue(localAccountRepository.exists(12345L));
        assertFalse(localAccountRepository.exists(54321L));
    }
}