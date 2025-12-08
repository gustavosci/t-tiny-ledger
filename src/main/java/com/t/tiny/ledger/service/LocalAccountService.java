package com.t.tiny.ledger.service;

import com.t.tiny.ledger.controller.dto.AccountBalanceDTO;
import com.t.tiny.ledger.controller.dto.AccountBaseInfoDTO;
import com.t.tiny.ledger.controller.dto.TransactionDTO;
import com.t.tiny.ledger.controller.request.CreateAccountRequest;
import com.t.tiny.ledger.controller.request.DepositRequest;
import com.t.tiny.ledger.controller.request.WithdrawRequest;
import com.t.tiny.ledger.exception.WithdrawForbiddenException;
import com.t.tiny.ledger.mapper.TransactionMapper;
import com.t.tiny.ledger.model.Account;
import com.t.tiny.ledger.model.Transaction;
import com.t.tiny.ledger.repository.AccountRepository;
import com.t.tiny.ledger.mapper.AccountMapper;
import com.t.tiny.ledger.repository.LocalAccountRepository;
import com.t.tiny.ledger.repository.LocalTransactionRepository;
import com.t.tiny.ledger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class LocalAccountService implements AccountService {

    private static final long MAX_ACCOUNT_NUMBER = 1000000000000000000L;

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public LocalAccountService(LocalAccountRepository accountRepository,
                               LocalTransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public AccountBaseInfoDTO createAccount(CreateAccountRequest createAccountRequest) {
        // TODO: RETRY ON CONFLICT
        long accountNumber = ThreadLocalRandom.current().nextLong(1, MAX_ACCOUNT_NUMBER);
        Account account = AccountMapper.fromCreateAccountRequest(accountNumber, createAccountRequest);
        accountRepository.createAccount(account);
        return AccountMapper.toBaseInfoDTO(account);
    }

    @Override
    public AccountBaseInfoDTO getAccountInfo(long accountNumber) {
        Account account = accountRepository.getAccount(accountNumber);
        return AccountMapper.toBaseInfoDTO(account);
    }

    @Override
    public AccountBalanceDTO getAccountBalance(long accountNumber) {
        Account account = accountRepository.getAccount(accountNumber);
        return AccountMapper.toBalanceDTO(account);
    }

    @Override
    public void deposit(long accountNumber, DepositRequest depositRequest) {
        Account account = accountRepository.getAccount(accountNumber);

        synchronized (account) {
            Transaction deposit = new Transaction(
                    accountNumber,
                    System.currentTimeMillis(),
                    Transaction.Operation.DEPOSIT,
                    depositRequest.amount().setScale(2, RoundingMode.HALF_UP)
            );
            transactionRepository.addTransaction(accountNumber, deposit);

            BigDecimal newBalance = account.getBalance().add(depositRequest.amount());
            account.setBalance(newBalance);
            accountRepository.updateAccount(account);
        }
    }

    @Override
    public void withdraw(long accountNumber, WithdrawRequest withdrawRequest) {
        Account account = accountRepository.getAccount(accountNumber);

        synchronized (account) {
            if (account.getBalance().compareTo(withdrawRequest.amount()) < 0) {
                throw new WithdrawForbiddenException("Insufficient balance.");
            }

            Transaction withdraw = new Transaction(
                    accountNumber,
                    System.currentTimeMillis(),
                    Transaction.Operation.WITHDRAW,
                    withdrawRequest.amount().setScale(2, RoundingMode.HALF_UP)
            );
            transactionRepository.addTransaction(accountNumber, withdraw);

            BigDecimal newBalance = account.getBalance().subtract(withdrawRequest.amount());
            account.setBalance(newBalance);
            accountRepository.updateAccount(account);
        }
    }

    @Override
    public List<TransactionDTO> getTransactions(long accountNumber) {
        // TODO ONLY VERIFY IF IT EXISTS
        Account account = accountRepository.getAccount(accountNumber);

        // TODO: NOT RETURNING IN STACK ORDER
        Collection<Transaction> transactions = transactionRepository.getTransactions(accountNumber);
        return TransactionMapper.toListOfTransactionDTO(transactions);
    }
}
