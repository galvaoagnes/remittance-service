package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;
import com.inter.remittance.ui.responses.TransactionResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionResponseMapperTest {

    @Test
    void shouldMapTransactionWithBothWallets() {
        Wallet source = wallet(Currency.BRL, new BigDecimal("500.00"));
        Wallet destination = wallet(Currency.USD, new BigDecimal("50.00"));

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                source,
                destination,
                new BigDecimal("30.00"),
                TransactionStatus.PENDING,
                TransactionType.REMITTANCE,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 5)
        );

        TransactionResponse response = TransactionResponseMapper.toResponse(transaction);

        assertNotNull(response);
        assertEquals(transaction.id(), response.id());
        assertEquals(source.id(), response.walletSourceId());
        assertEquals(destination.id(), response.walletDestinationId());
        assertEquals(new BigDecimal("30.00"), response.amount());
        assertEquals(TransactionStatus.PENDING, response.status());
        assertEquals(TransactionType.REMITTANCE, response.type());
    }

    @Test
    void shouldMapDepositTransactionWithNullSourceWallet() {
        Wallet destination = wallet(Currency.BRL, new BigDecimal("100.00"));

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                null,
                destination,
                new BigDecimal("100.00"),
                TransactionStatus.CREATED,
                TransactionType.DEPOSIT,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 0)
        );

        TransactionResponse response = TransactionResponseMapper.toResponse(transaction);

        assertNotNull(response);
        assertNull(response.walletSourceId());
        assertEquals(destination.id(), response.walletDestinationId());
        assertEquals(TransactionType.DEPOSIT, response.type());
    }

    @Test
    void shouldMapWithdrawalTransactionWithNullDestinationWallet() {
        Wallet source = wallet(Currency.BRL, new BigDecimal("200.00"));

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                source,
                null,
                new BigDecimal("50.00"),
                TransactionStatus.CREATED,
                TransactionType.WITHDRAWAL,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 0)
        );

        TransactionResponse response = TransactionResponseMapper.toResponse(transaction);

        assertNotNull(response);
        assertEquals(source.id(), response.walletSourceId());
        assertNull(response.walletDestinationId());
        assertEquals(TransactionType.WITHDRAWAL, response.type());
    }

    @Test
    void shouldMapFinishedWithSuccessStatus() {
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                wallet(Currency.BRL, new BigDecimal("100.00")),
                wallet(Currency.USD, new BigDecimal("10.00")),
                new BigDecimal("10.00"),
                TransactionStatus.FINISHED_WITH_SUCCESS,
                TransactionType.REMITTANCE,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 0)
        );

        TransactionResponse response = TransactionResponseMapper.toResponse(transaction);

        assertEquals(TransactionStatus.FINISHED_WITH_SUCCESS, response.status());
    }

    @Test
    void shouldMapFinishedWithFailureStatus() {
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                wallet(Currency.BRL, new BigDecimal("100.00")),
                wallet(Currency.USD, BigDecimal.ZERO),
                new BigDecimal("10.00"),
                TransactionStatus.FINISHED_WITH_FAILURE,
                TransactionType.REMITTANCE,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 0)
        );

        TransactionResponse response = TransactionResponseMapper.toResponse(transaction);

        assertEquals(TransactionStatus.FINISHED_WITH_FAILURE, response.status());
    }

    @Test
    void shouldReturnNullWhenTransactionIsNull() {
        assertNull(TransactionResponseMapper.toResponse(null));
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

