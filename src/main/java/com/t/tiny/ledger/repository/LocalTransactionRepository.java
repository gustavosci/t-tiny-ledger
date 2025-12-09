package com.t.tiny.ledger.repository;

import com.t.tiny.ledger.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Repository
public class LocalTransactionRepository implements TransactionRepository {

    // IF SEARCH BY TIMESTAMP SHOULD BE SUPPORTED, USE A NavigableMap INSTEAD OF Deque
    private final Map<Long, Deque<Transaction>> ledger = new ConcurrentHashMap<>();

    @Override
    public void addTransaction(long accountNumber, Transaction transaction) {
        ledger.computeIfAbsent(accountNumber, k -> new ConcurrentLinkedDeque<>())
                .addFirst(transaction);
    }

    @Override
    public Collection<Transaction> getTransactions(long accountNumber) {
        return ledger.getOrDefault(accountNumber, new ConcurrentLinkedDeque<>());
    }
}
