package com.t.tiny.ledger.mapper;

import com.t.tiny.ledger.controller.dto.TransactionDTO;
import com.t.tiny.ledger.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {

    @Test
    void shouldMapListOfTransactionsToListOfTransactionDTOs() {
        // Given
        var transaction1 = new Transaction(123L, Instant.now().toEpochMilli(), Transaction.Operation.DEPOSIT, BigDecimal.TEN);
        var transaction2 = new Transaction(123L, Instant.now().plusSeconds(60).toEpochMilli(), Transaction.Operation.WITHDRAW, BigDecimal.ONE);
        var transactions = List.of(transaction1, transaction2);

        // When
        List<TransactionDTO> result = TransactionMapper.toListOfTransactionDTO(transactions);

        // Then
        assertThat(result).isNotNull().hasSize(2);

        assertThat(result.get(0).accountNumber()).isEqualTo(transaction1.accountNumber());
        assertThat(result.get(0).timestamp()).isEqualTo(transaction1.timestamp());
        assertThat(result.get(0).operation()).isEqualTo(transaction1.operation());
        assertThat(result.get(0).amount()).isEqualTo(transaction1.amount());

        assertThat(result.get(1).accountNumber()).isEqualTo(transaction2.accountNumber());
        assertThat(result.get(1).timestamp()).isEqualTo(transaction2.timestamp());
        assertThat(result.get(1).operation()).isEqualTo(transaction2.operation());
        assertThat(result.get(1).amount()).isEqualTo(transaction2.amount());
    }

    @Test
    void shouldReturnEmptyListForEmptyTransactionList() {
        // Given
        var transactions = Collections.<Transaction>emptyList();

        // When
        List<TransactionDTO> result = TransactionMapper.toListOfTransactionDTO(transactions);

        // Then
        assertThat(result).isNotNull().isEmpty();
    }
}