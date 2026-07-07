package com.inter.remittance.domain.entities;

import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Transaction (
        UUID id,
        Wallet sourceWallet,
        Wallet destinationWallet,
        BigDecimal amount,
        TransactionStatus status,
        TransactionType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){

    public static Transaction createNewTransaction(Wallet sourceWallet, Wallet destinationWallet, BigDecimal amount, TransactionType type) {
        validateWalletSource(sourceWallet, type);
        validateWalletDestination(destinationWallet, type
        );

        return new Transaction(
                UUID.randomUUID(),
                sourceWallet,
                destinationWallet,
                amount,
                TransactionStatus.CREATED,
                type,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
    static void validateWalletSource(Wallet walletSource, TransactionType type) {
        if (walletSource == null &&
                (type == TransactionType.WITHDRAWAL || type == TransactionType.REMITTANCE)) {
            throw new IllegalArgumentException("Wallet source cannot be null");
        }
    }

    static void validateWalletDestination(Wallet walletDestination, TransactionType type) {
        if (walletDestination == null &&
                (type == TransactionType.DEPOSIT || type == TransactionType.REMITTANCE)) {
            throw new IllegalArgumentException("Wallet destination cannot be null");
        }
    }

    public Transaction transitionStatusTo(Transaction transaction, TransactionStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        if (!transaction.status.canTransitionTo(newStatus)) {
            throw new IllegalArgumentException(
                    "Invalid transaction status transition from " + transaction.status + " to " + newStatus
            );
        }

        return new Transaction(
                transaction.id,
                transaction.sourceWallet,
                transaction.destinationWallet,
                transaction.amount,
                newStatus,
                transaction.type,
                transaction.createdAt,
                transaction.updatedAt
        );

    }


}
