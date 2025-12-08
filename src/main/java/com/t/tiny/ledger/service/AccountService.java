package com.t.tiny.ledger.service;

import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.controller.dto.AccountDTO;

/**
 * Service interface for account management operations.
 */
public interface AccountService {

    /**
     * Creates a new account based on the provided request data.
     *
     * @param createAccountRequest the request object containing account details
     * @return the created account details as an AccountDTO
     */
    AccountDTO createAccount(CreateAccountRequest createAccountRequest);

    /**
     * Retrieves account details by the given account number.
     *
     * @param accountNumber the unique identifier of the account
     * @return the account details as an AccountDTO
     */
    AccountDTO getAccount(long accountNumber);
}

