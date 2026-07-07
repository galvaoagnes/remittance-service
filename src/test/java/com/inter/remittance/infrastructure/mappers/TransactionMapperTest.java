package com.inter.remittance.infrastructure.mappers;

import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;
import com.inter.remittance.infrastructure.persistence.entities.TransactionEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionMapperTest {

    @Test
    void shouldMapDomainToEntity() {
        Wallet source = wallet(Currency.BRL, new BigDecimal("150.00"));
        Wallet destination = wallet(Currency.USD, new BigDecimal("30.00"));

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                source,
                destination,
                new BigDecimal("45.50"),
                TransactionStatus.PENDING,
                TransactionType.REMITTANCE,
                LocalDateTime.of(2026, 1, 1, 12, 0),
                LocalDateTime.of(2026, 1, 1, 12, 5)
        );

        TransactionEntity entity = TransactionMapper.toEntity(transaction);

        assertNotNull(entity);
        assertEquals(transaction.id(), entity.getId());
        assertEquals(source.id(), entity.getSourceWallet().getId());
        assertEquals(destination.id(), entity.getDestinationWallet().getId());
        assertEquals(transaction.amount(), entity.getAmount());
        assertEquals(transaction.status(), entity.getStatus());
        assertEquals(transaction.type(), entity.getType());
        assertEquals(transaction.createdAt(), entity.getCreatedAt());
        assertEquals(transaction.updatedAt(), entity.getUpdatedAt());
    }

    @Test
    void shouldMapDomainToEntityWithNullWallets() {
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                null,
                wallet(Currency.BRL, BigDecimal.ZERO),
                new BigDecimal("10.00"),
                TransactionStatus.CREATED,
                TransactionType.DEPOSIT,
                LocalDateTime.of(2026, 2, 1, 10, 0),
                LocalDateTime.of(2026, 2, 1, 10, 1)
        );

        TransactionEntity entity = TransactionMapper.toEntity(transaction);

        assertNull(entity.getSourceWallet());
        assertNotNull(entity.getDestinationWallet());
    }

    @Test
    void shouldMapEntityToDomain() {
        UUID sourceId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();

        TransactionEntity entity = new TransactionEntity(
                UUID.randomUUID(),
                new com.inter.remittance.infrastructure.persistence.entities.WalletEntity(
                        sourceId,
                        Currency.BRL,
                        new BigDecimal("150.00"),
                        LocalDateTime.of(2026, 1, 1, 10, 0),
                        LocalDateTime.of(2026, 1, 1, 10, 5)
                ),
                new com.inter.remittance.infrastructure.persistence.entities.WalletEntity(
                        destinationId,
                        Currency.USD,
                        new BigDecimal("30.00"),
                        LocalDateTime.of(2026, 1, 1, 11, 0),
                        LocalDateTime.of(2026, 1, 1, 11, 5)
                ),
                new BigDecimal("45.50"),
                TransactionStatus.FINISHED_WITH_SUCCESS,
                TransactionType.REMITTANCE,
                LocalDateTime.of(2026, 1, 2, 12, 0),
                LocalDateTime.of(2026, 1, 2, 12, 5)
        );

        Transaction domain = TransactionMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.id());
        assertEquals(sourceId, domain.sourceWallet().id());
        assertEquals(destinationId, domain.destinationWallet().id());
        assertEquals(entity.getAmount(), domain.amount());
        assertEquals(entity.getStatus(), domain.status());
        assertEquals(entity.getType(), domain.type());
        assertEquals(entity.getCreatedAt(), domain.createdAt());
        assertEquals(entity.getUpdatedAt(), domain.updatedAt());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(TransactionMapper.toDomain(null));
    }

    private Wallet wallet(Currency currency, BigDecimal balance) {
        return new Wallet(
                UUID.randomUUID(),
                currency,
                balance,
                new HashSet<>(),
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 5)
        );
    }
}

