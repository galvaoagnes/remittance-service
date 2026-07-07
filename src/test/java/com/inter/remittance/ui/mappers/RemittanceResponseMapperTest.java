package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.Remittance;
import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;
import com.inter.remittance.ui.responses.RemittanceResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RemittanceResponseMapperTest {

    @Test
    void shouldMapTransactionAndRemittanceToResponse() {
        Wallet source = wallet(Currency.BRL, new BigDecimal("1000.00"));
        Wallet destination = wallet(Currency.USD, new BigDecimal("100.00"));

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                source,
                destination,
                new BigDecimal("100.00"),
                TransactionStatus.FINISHED_WITH_SUCCESS,
                TransactionType.REMITTANCE,
                LocalDateTime.of(2026, 1, 1, 12, 0),
                LocalDateTime.of(2026, 1, 1, 12, 5)
        );

        Remittance remittance = new Remittance(
                UUID.randomUUID(),
                transaction,
                new BigDecimal("18.50"),
                new BigDecimal("0.185"),
                LocalDateTime.of(2026, 1, 1, 12, 0),
                LocalDateTime.of(2026, 1, 1, 12, 5)
        );

        RemittanceResponse response = RemittanceResponseMapper.toResponse(Map.of(transaction, remittance));

        assertNotNull(response);
        assertEquals(remittance.id(), response.remittanceId());
        assertEquals(transaction.id(), response.transactionId());
        assertEquals(new BigDecimal("100.00"), response.originalAmount());
        assertEquals(new BigDecimal("18.50"), response.convertedAmount());
        assertEquals(new BigDecimal("0.185"), response.exchangeRate());
        assertEquals(TransactionStatus.FINISHED_WITH_SUCCESS, response.transactionStatus());
        assertEquals(LocalDateTime.of(2026, 1, 1, 12, 0), response.createdAt());
    }

    @Test
    void shouldReturnNullWhenRemittanceValueIsNull() {
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                wallet(Currency.BRL, new BigDecimal("100.00")),
                wallet(Currency.USD, new BigDecimal("10.00")),
                new BigDecimal("10.00"),
                TransactionStatus.CREATED,
                TransactionType.REMITTANCE,
                LocalDateTime.of(2026, 1, 1, 12, 0),
                LocalDateTime.of(2026, 1, 1, 12, 5)
        );

        Map<Transaction, Remittance> result = new HashMap<>();
        result.put(transaction, null);

        assertNull(RemittanceResponseMapper.toResponse(result));
    }

    @Test
    void shouldPreserveFinishedWithFailureStatus() {
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                wallet(Currency.BRL, new BigDecimal("100.00")),
                wallet(Currency.USD, BigDecimal.ZERO),
                new BigDecimal("10.00"),
                TransactionStatus.FINISHED_WITH_FAILURE,
                TransactionType.REMITTANCE,
                LocalDateTime.of(2026, 1, 1, 12, 0),
                LocalDateTime.of(2026, 1, 1, 12, 5)
        );

        Remittance remittance = new Remittance(
                UUID.randomUUID(),
                transaction,
                BigDecimal.ZERO,
                new BigDecimal("0.185"),
                LocalDateTime.of(2026, 1, 1, 12, 0),
                LocalDateTime.of(2026, 1, 1, 12, 5)
        );

        RemittanceResponse response = RemittanceResponseMapper.toResponse(Map.of(transaction, remittance));

        assertNotNull(response);
        assertEquals(TransactionStatus.FINISHED_WITH_FAILURE, response.transactionStatus());
    }

    private static Wallet wallet(Currency currency, BigDecimal balance) {
        return new Wallet(
                UUID.randomUUID(),
                currency,
                balance,
                Set.of(),
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 0)
        );
    }
}

