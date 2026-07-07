package com.inter.remittance.domain.entities;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionTest {

    @Test
    void shouldCreateDepositTransactionWithoutSourceWallet() {
        Wallet destinationWallet = validWallet(Currency.BRL, new BigDecimal("100.00"));

        Transaction transaction = Transaction.createNewTransaction(
                null,
                destinationWallet,
                new BigDecimal("25.50"),
                TransactionType.DEPOSIT
        );

        assertNotNull(transaction.id());
        assertEquals(destinationWallet, transaction.destinationWallet());
        assertEquals(TransactionStatus.CREATED, transaction.status());
        assertEquals(TransactionType.DEPOSIT, transaction.type());
        assertEquals(0, transaction.amount().compareTo(new BigDecimal("25.50")));
        assertNotNull(transaction.createdAt());
        assertNotNull(transaction.updatedAt());
    }

    @Test
    void shouldCreateWithdrawalTransactionWithoutDestinationWallet() {
        Wallet sourceWallet = validWallet(Currency.BRL, new BigDecimal("100.00"));

        Transaction transaction = Transaction.createNewTransaction(
                sourceWallet,
                null,
                new BigDecimal("10.00"),
                TransactionType.WITHDRAWAL
        );

        assertEquals(sourceWallet, transaction.sourceWallet());
        assertEquals(TransactionStatus.CREATED, transaction.status());
        assertEquals(TransactionType.WITHDRAWAL, transaction.type());
    }

    @Test
    void shouldCreateRemittanceTransactionWithBothWallets() {
        Wallet sourceWallet = validWallet(Currency.BRL, new BigDecimal("100.00"));
        Wallet destinationWallet = validWallet(Currency.USD, BigDecimal.ZERO);

        Transaction transaction = Transaction.createNewTransaction(
                sourceWallet,
                destinationWallet,
                new BigDecimal("10.00"),
                TransactionType.REMITTANCE
        );

        assertEquals(sourceWallet, transaction.sourceWallet());
        assertEquals(destinationWallet, transaction.destinationWallet());
        assertEquals(TransactionStatus.CREATED, transaction.status());
    }

    @Test
    void shouldThrowWhenSourceWalletIsNullForWithdrawal() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Transaction.createNewTransaction(
                        null,
                        validWallet(Currency.USD, BigDecimal.ZERO),
                        new BigDecimal("10.00"),
                        TransactionType.WITHDRAWAL
                )
        );

        assertEquals("Wallet source cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowWhenDestinationWalletIsNullForDeposit() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Transaction.createNewTransaction(
                        validWallet(Currency.BRL, new BigDecimal("10.00")),
                        null,
                        new BigDecimal("10.00"),
                        TransactionType.DEPOSIT
                )
        );

        assertEquals("Wallet destination cannot be null", ex.getMessage());
    }

    @Test
    void shouldTransitionFromCreatedToPending() {
        Transaction transaction = baseTransaction(TransactionStatus.CREATED);

        Transaction transitioned = transaction.transitionStatusTo(transaction, TransactionStatus.PENDING);

        assertEquals(TransactionStatus.PENDING, transitioned.status());
        assertEquals(transaction.id(), transitioned.id());
    }

    @Test
    void shouldTransitionFromPendingToSuccess() {
        Transaction transaction = baseTransaction(TransactionStatus.PENDING);

        Transaction transitioned = transaction.transitionStatusTo(transaction, TransactionStatus.FINISHED_WITH_SUCCESS);

        assertEquals(TransactionStatus.FINISHED_WITH_SUCCESS, transitioned.status());
    }

    @Test
    void shouldThrowWhenTransitionIsInvalid() {
        Transaction transaction = baseTransaction(TransactionStatus.CREATED);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transaction.transitionStatusTo(transaction, TransactionStatus.FINISHED_WITH_SUCCESS)
        );

        assertEquals("Invalid transaction status transition from CREATED to FINISHED_WITH_SUCCESS", ex.getMessage());
    }

    @Test
    void shouldThrowWhenTransitionStatusIsNull() {
        Transaction transaction = baseTransaction(TransactionStatus.CREATED);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transaction.transitionStatusTo(transaction, null)
        );

        assertEquals("Status cannot be null", ex.getMessage());
    }

    private Transaction baseTransaction(TransactionStatus status) {
        Wallet sourceWallet = validWallet(Currency.BRL, new BigDecimal("100.00"));
        Wallet destinationWallet = validWallet(Currency.USD, BigDecimal.ZERO);
        return new Transaction(
                UUID.randomUUID(),
                sourceWallet,
                destinationWallet,
                new BigDecimal("10.00"),
                status,
                TransactionType.REMITTANCE,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 5)
        );
    }

    private Wallet validWallet(Currency currency, BigDecimal balance) {
        return new Wallet(
                UUID.randomUUID(),
                currency,
                balance,
                new HashSet<>(),
                LocalDateTime.of(2026, 1, 1, 9, 0),
                LocalDateTime.of(2026, 1, 1, 9, 5)
        );
    }
}

