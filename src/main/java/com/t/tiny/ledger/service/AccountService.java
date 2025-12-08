package com.t.tiny.ledger.service;

import com.t.tiny.ledger.controller.dto.AccountBalanceDTO;
import com.t.tiny.ledger.controller.dto.TransactionDTO;
import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.controller.dto.AccountBaseInfoDTO;
import com.t.tiny.ledger.controller.request.DepositRequest;
import com.t.tiny.ledger.controller.request.WithdrawRequest;

import java.util.List;

/**
 * Service interface for account management operations.
 */
public interface AccountService {

    /**
     * Creates a new account based on the provided request data.
     *
     * @param createAccountRequest the request object containing account details
     * @return the created account basic information
     */
    AccountBaseInfoDTO createAccount(CreateAccountRequest createAccountRequest);

    /**
     * Retrieves account details by the given account number.
     *
     * @param accountNumber the unique identifier of the account
     * @return the account basic information
     */
    AccountBaseInfoDTO getAccountInfo(long accountNumber);

    AccountBalanceDTO getAccountBalance(long accountNumber);

    void deposit(long accountNumber, DepositRequest depositRequest);

    void withdraw(long accountNumber, WithdrawRequest withdrawRequest);

    List<TransactionDTO> getTransactions(long accountNumber);
}

