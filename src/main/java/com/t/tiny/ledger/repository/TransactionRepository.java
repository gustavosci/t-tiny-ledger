package com.t.tiny.ledger.repository;

import com.t.tiny.ledger.model.Transaction;

import java.util.Collection;

/**
 * Repository interface for transaction data access operations.
 */
public interface TransactionRepository {

    /**
     * Adds a new transaction for the given account number.
     *
     * @param accountNumber the account number
     * @param transaction the transaction to add
     */
    void addTransaction(long accountNumber, Transaction transaction);

    /**
     * Retrieves all transactions for the given account number.
     *
     * @param accountNumber the account number
     * @return a collection of transactions
     */
    Collection<Transaction> getTransactions(long accountNumber);
}
