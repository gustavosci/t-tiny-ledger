package com.t.tiny.ledger.repository;

import com.t.tiny.ledger.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalTransactionRepositoryTest {

    private LocalTransactionRepository localTransactionRepository;

    @BeforeEach
    void setUp() {
        localTransactionRepository = new LocalTransactionRepository();
    }

    @Test
    void validateAddingMultipleTransactionsAndThenGetTransactionsInOrderByLatest() {
        long accountNumber = 12345L;
        Transaction deposit = new Transaction(accountNumber, System.currentTimeMillis(), Transaction.Operation.DEPOSIT, BigDecimal.valueOf(100));
        localTransactionRepository.addTransaction(accountNumber, deposit);
        Transaction withdraw = new Transaction(accountNumber, System.currentTimeMillis(), Transaction.Operation.WITHDRAW, BigDecimal.valueOf(50));
        localTransactionRepository.addTransaction(accountNumber, withdraw);


        Collection<Transaction> transactions = localTransactionRepository.getTransactions(accountNumber);
        assertEquals(2, transactions.size());

        Iterator<Transaction> iterator = transactions.iterator();
        assertEquals(withdraw, iterator.next());
        assertEquals(deposit, iterator.next());
    }

    @Test
    void validateEmptyTransactions() {
        Collection<Transaction> transactions = localTransactionRepository.getTransactions(12345L);
        assertTrue(transactions.isEmpty());
    }
}