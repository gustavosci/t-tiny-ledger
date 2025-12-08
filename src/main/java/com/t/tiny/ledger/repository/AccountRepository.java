package com.t.tiny.ledger.repository;

import com.t.tiny.ledger.model.Account;

/**
 * Repository interface for account data access operations.
 */
public interface AccountRepository {

    /**
     * Checks if an account with the given account number exists.
     *
     * @param accountNumber the account number to check
     * @return true if the account exists, false otherwise
     */
    boolean exists(long accountNumber);

    /**
     * Creates a new account.
     *
     * @param account the account to create
     */
    void createAccount(Account account);

    /**
     * Updates an existing account.
     *
     * @param account the account to update
     */
    void updateAccount(Account account);

    /**
     * Retrieves an account by its account number.
     *
     * @param accountNumber the account number
     * @return the account object
     */
    Account getAccount(long accountNumber);
}
