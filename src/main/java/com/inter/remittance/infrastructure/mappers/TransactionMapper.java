package com.inter.remittance.infrastructure.mappers;

import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.infrastructure.persistence.entities.TransactionEntity;

public class TransactionMapper {
    private TransactionMapper(){}

    public static TransactionEntity toEntity(Transaction transaction) {
        return new TransactionEntity(
                transaction.id(),
                WalletMapper.toEntity(transaction.sourceWallet()),
                WalletMapper.toEntity(transaction.destinationWallet()),
                transaction.amount(),
                transaction.status(),
                transaction.type(),
                transaction.createdAt(),
                transaction.updatedAt()
        );
    }

    public static Transaction toDomain(TransactionEntity entity) {
        if (entity == null) return null;

        return new Transaction(
                entity.getId(),
                WalletMapper.toDomain(entity.getSourceWallet()),
                WalletMapper.toDomain(entity.getDestinationWallet()),
                entity.getAmount(),
                entity.getStatus(),
                entity.getType(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
