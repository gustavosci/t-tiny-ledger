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

    private final Map<Long, Deque<Transaction>> transactionMap = new ConcurrentHashMap<>();

    @Override
    public void addTransaction(long accountNumber, Transaction transaction) {
        transactionMap.computeIfAbsent(accountNumber, k -> new ConcurrentLinkedDeque<>())
                .addFirst(transaction);
    }

    @Override
    public Collection<Transaction> getTransactions(long accountNumber) {
        return transactionMap.getOrDefault(accountNumber, new ConcurrentLinkedDeque<>());
    }
}
