package com.t.tiny.ledger.repository;

import com.t.tiny.ledger.model.Transaction;

import java.util.Collection;

public interface TransactionRepository {

    void addTransaction(long accountNumber, Transaction transaction);

    Collection<Transaction> getTransactions(long accountNumber);
}
