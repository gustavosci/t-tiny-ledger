package com.t.tiny.ledger.mapper;

import com.t.tiny.ledger.controller.dto.TransactionDTO;
import com.t.tiny.ledger.model.Transaction;

import java.util.Collection;
import java.util.List;

public final class TransactionMapper {

    private TransactionMapper() {}

    public static List<TransactionDTO> toListOfTransactionDTO(Collection<Transaction> transactions) {
        return transactions.stream()
                .map(t -> new TransactionDTO(t.accountNumber(), t.timestamp(), t.operation(), t.amount()))
                .toList();
    }
}