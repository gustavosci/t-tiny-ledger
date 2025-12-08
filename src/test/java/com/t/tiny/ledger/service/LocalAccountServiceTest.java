package com.t.tiny.ledger.service;

import com.t.tiny.ledger.controller.dto.AccountBalanceDTO;
import com.t.tiny.ledger.controller.dto.AccountBaseInfoDTO;
import com.t.tiny.ledger.controller.dto.TransactionDTO;
import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.controller.request.DepositRequest;
import com.t.tiny.ledger.controller.request.WithdrawRequest;
import com.t.tiny.ledger.exception.AccountNotFoundException;
import com.t.tiny.ledger.exception.TinyLedgerException;
import com.t.tiny.ledger.exception.WithdrawForbiddenException;
import com.t.tiny.ledger.model.Account;
import com.t.tiny.ledger.model.Transaction;
import com.t.tiny.ledger.repository.LocalAccountRepository;
import com.t.tiny.ledger.repository.LocalTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class LocalAccountServiceTest {

    @Mock
    private LocalAccountRepository accountRepository;

    @Mock
    private LocalTransactionRepository transactionRepository;

    private AccountService accountService;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    @BeforeEach
    void setUp() {
        accountService = new LocalAccountService(accountRepository, transactionRepository);
    }

    @Test
    void givenCreateAccountRequest_whenCreateAccount_thenNewAccountIsCreatedAndReturned() {
        // Given
        LocalDate dob = LocalDate.of(1990, 1, 1);
        CreateAccountRequest request = new CreateAccountRequest("John Doe", dob, "123 Main St");
        given(accountRepository.exists(anyLong())).willReturn(false);

        // When
        AccountBaseInfoDTO result = accountService.createAccount(request);

        // Then
        then(accountRepository).should().createAccount(accountCaptor.capture());
        Account capturedAccount = accountCaptor.getValue();

        assertThat(result).isNotNull();
        assertThat(result.fullName()).isEqualTo(request.fullName());
        assertThat(result.address()).isEqualTo(request.address());
        assertThat(result.dateOfBirth()).isEqualTo(request.dateOfBirth());

        assertThat(result.accountNumber()).isEqualTo(capturedAccount.getAccountNumber());
        assertThat(capturedAccount.getBalance()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void givenExistingAccountNumber_whenGetAccountInfo_thenAccountInfoIsReturned() {
        // Given
        LocalDate dob = LocalDate.of(1985, 5, 15);
        Account account = new Account(123L, "Jane Doe", dob, "456 Oak Ave");
        given(accountRepository.getAccount(account.getAccountNumber())).willReturn(account);

        // When
        AccountBaseInfoDTO result = accountService.getAccountInfo(account.getAccountNumber());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.accountNumber()).isEqualTo(account.getAccountNumber());
        assertThat(result.fullName()).isEqualTo(account.getFullName());
        assertThat(result.address()).isEqualTo(account.getAddress());
        assertThat(result.dateOfBirth()).isEqualTo(account.getDateOfBirth());
    }

    @Test
    void givenExistingAccountNumber_whenGetAccountBalance_thenAccountBalanceIsReturned() {
        // Given
        long accountNumber = 123L;
        LocalDate dob = LocalDate.of(1985, 5, 15);
        Account account = new Account(accountNumber, "Jane Doe", dob, "456 Oak Ave");
        account.setBalance(BigDecimal.TEN);
        given(accountRepository.getAccount(accountNumber)).willReturn(account);

        // When
        AccountBalanceDTO result = accountService.getAccountBalance(accountNumber);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.accountNumber()).isEqualTo(accountNumber);
        assertThat(result.fullName()).isEqualTo(account.getFullName());
        assertThat(result.balance()).isEqualByComparingTo("10.00");
    }

    @Test
    void givenDepositRequest_whenDeposit_thenBalanceIsIncreased() {
        // Given
        long accountNumber = 123L;
        LocalDate dob = LocalDate.of(1985, 5, 15);
        Account account = new Account(accountNumber, "Jane Doe", dob, "456 Oak Ave");
        account.setBalance(BigDecimal.TEN);
        DepositRequest request = new DepositRequest(BigDecimal.valueOf(5.99));
        given(accountRepository.getAccount(accountNumber)).willReturn(account);

        // When
        accountService.deposit(accountNumber, request);

        // Then
        then(transactionRepository).should().addTransaction(eq(accountNumber), transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();
        assertThat(capturedTransaction.operation()).isEqualTo(Transaction.Operation.DEPOSIT);
        assertThat(capturedTransaction.amount()).isEqualByComparingTo("5.99");
        assertThat(capturedTransaction.timestamp()).isGreaterThan(0);
        assertThat(capturedTransaction.accountNumber()).isEqualTo(accountNumber);

        then(accountRepository).should().updateAccount(accountCaptor.capture());
        Account updatedAccount = accountCaptor.getValue();
        assertThat(updatedAccount.getBalance()).isEqualByComparingTo("15.99");
    }

    @Test
    void givenWithdrawRequest_whenWithdraw_thenBalanceIsDecreased() {
        // Given
        long accountNumber = 123L;
        LocalDate dob = LocalDate.of(1985, 5, 15);
        Account account = new Account(accountNumber, "Jane Doe", dob, "456 Oak Ave");
        account.setBalance(BigDecimal.TEN);
        WithdrawRequest request = new WithdrawRequest(BigDecimal.valueOf(9.97));
        given(accountRepository.getAccount(accountNumber)).willReturn(account);

        // When
        accountService.withdraw(accountNumber, request);

        // Then
        then(transactionRepository).should().addTransaction(eq(accountNumber), transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();
        assertThat(capturedTransaction.operation()).isEqualTo(Transaction.Operation.WITHDRAW);
        assertThat(capturedTransaction.amount()).isEqualByComparingTo("9.97");
        assertThat(capturedTransaction.timestamp()).isGreaterThan(0);
        assertThat(capturedTransaction.accountNumber()).isEqualTo(accountNumber);

        then(accountRepository).should().updateAccount(accountCaptor.capture());
        Account updatedAccount = accountCaptor.getValue();
        assertThat(updatedAccount.getBalance()).isEqualByComparingTo("0.03");
    }

    @Test
    void givenWithdrawRequestWithZeroAmount_whenWithdraw_thenThrowsException() {
        // Given
        long accountNumber = 123L;
        LocalDate dob = LocalDate.of(1985, 5, 15);
        Account account = new Account(accountNumber, "Jane Doe", dob, "456 Oak Ave");
        account.setBalance(BigDecimal.TEN);
        WithdrawRequest request = new WithdrawRequest(BigDecimal.ZERO);
        given(accountRepository.getAccount(accountNumber)).willReturn(account);

        // When & Then
        assertThrows(WithdrawForbiddenException.class, () -> accountService.withdraw(accountNumber, request));
        then(transactionRepository).should(never()).addTransaction(anyLong(), any());
        then(accountRepository).should(never()).updateAccount(any());
    }

    @Test
    void givenWithdrawRequestWithNegativeAmount_whenWithdraw_thenThrowsException() {
        // Given
        long accountNumber = 123L;
        LocalDate dob = LocalDate.of(1985, 5, 15);
        Account account = new Account(accountNumber, "Jane Doe", dob, "456 Oak Ave");
        account.setBalance(BigDecimal.TEN);
        WithdrawRequest request = new WithdrawRequest(BigDecimal.valueOf(-15));
        given(accountRepository.getAccount(accountNumber)).willReturn(account);

        // When & Then
        assertThrows(WithdrawForbiddenException.class, () -> accountService.withdraw(accountNumber, request));
        then(transactionRepository).should(never()).addTransaction(anyLong(), any());
        then(accountRepository).should(never()).updateAccount(any());
    }

    @Test
    void givenWithdrawRequestWithInsufficientBalance_whenWithdraw_thenThrowsException() {
        // Given
        long accountNumber = 123L;
        LocalDate dob = LocalDate.of(1985, 5, 15);
        Account account = new Account(accountNumber, "Jane Doe", dob, "456 Oak Ave");
        account.setBalance(BigDecimal.TEN);
        WithdrawRequest request = new WithdrawRequest(BigDecimal.valueOf(15));
        given(accountRepository.getAccount(accountNumber)).willReturn(account);

        // When & Then
        assertThrows(WithdrawForbiddenException.class, () -> accountService.withdraw(accountNumber, request));
        then(transactionRepository).should(never()).addTransaction(anyLong(), any());
        then(accountRepository).should(never()).updateAccount(any());
    }

    @Test
    void givenExistingAccountNumber_whenGetTransactions_thenReturnsTransactionsInOrder() {
        // Given
        long accountNumber = 123L;
        Transaction t1 = new Transaction(accountNumber, System.currentTimeMillis(), Transaction.Operation.DEPOSIT, BigDecimal.TEN);
        Transaction t2 = new Transaction(accountNumber, System.currentTimeMillis(), Transaction.Operation.WITHDRAW, BigDecimal.valueOf(9));
        given(accountRepository.exists(accountNumber)).willReturn(true);
        given(transactionRepository.getTransactions(accountNumber)).willReturn(List.of(t1, t2));

        // When
        List<TransactionDTO> result = accountService.getTransactions(accountNumber);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).amount()).isEqualTo(BigDecimal.TEN);
        assertThat(result.get(0).operation()).isEqualTo(Transaction.Operation.DEPOSIT);
        assertThat(result.get(1).amount()).isEqualTo(BigDecimal.valueOf(9));
        assertThat(result.get(1).operation()).isEqualTo(Transaction.Operation.WITHDRAW);
    }

    @Test
    void givenNonExistingAccountNumber_whenGetTransactions_thenThrowsException() {
        // Given
        long accountNumber = 123L;
        given(accountRepository.exists(accountNumber)).willReturn(false);

        // When & Then
        assertThrows(AccountNotFoundException.class, () -> accountService.getTransactions(accountNumber));
        then(transactionRepository).should(never()).getTransactions(anyLong());
    }

    @Test
    void givenUnableToGenerateAccountNumber_whenCreateAccount_thenThrowsException() {
        // Given
        LocalDate dob = LocalDate.of(1990, 1, 1);
        CreateAccountRequest request = new CreateAccountRequest("John Doe", dob, "123 Main St");
        given(accountRepository.exists(anyLong())).willReturn(true);

        // When & Then
        assertThrows(TinyLedgerException.class, () -> accountService.createAccount(request));
        then(accountRepository).should(times(10)).exists(anyLong());
        then(accountRepository).should(never()).createAccount(any());
    }
}