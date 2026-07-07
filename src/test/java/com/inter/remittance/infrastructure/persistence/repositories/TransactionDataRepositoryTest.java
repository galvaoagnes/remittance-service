package com.inter.remittance.infrastructure.persistence.repositories;

import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;
import com.inter.remittance.infrastructure.persistence.entities.TransactionEntity;
import com.inter.remittance.infrastructure.persistence.entities.WalletEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionDataRepositoryTest {

    @Mock
    private TransactionJpaRepository jpaRepository;

    private TransactionDataRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TransactionDataRepository(jpaRepository);
    }

    @Test
    void shouldFindTransactionsByWalletIdWithPagination() {
        UUID walletId = UUID.randomUUID();

        TransactionEntity entity = transactionEntity(
                UUID.randomUUID(),
                walletId,
                UUID.randomUUID(),
                new BigDecimal("50.00"),
                TransactionStatus.FINISHED_WITH_SUCCESS,
                TransactionType.DEPOSIT
        );

        PageImpl<TransactionEntity> page = new PageImpl<>(
                List.of(entity),
                PageRequest.of(1, 5),
                11
        );

        when(jpaRepository.findByWalletId(walletId, PageRequest.of(1, 5))).thenReturn(page);

        PageResult<Transaction> result = repository.findTransactionsByWalletId(walletId, 1, 5);

        assertEquals(1, result.page());
        assertEquals(5, result.size());
        assertEquals(11, result.totalElements());
        assertEquals(3, result.totalPages());
        assertEquals(1, result.content().size());

        Transaction transaction = result.content().iterator().next();
        assertEquals(entity.getId(), transaction.id());
        assertEquals(entity.getStatus(), transaction.status());
        assertEquals(entity.getType(), transaction.type());
    }

    @Test
    void shouldSaveTransactionMappingDomainAndReturningMappedValue() {
        Transaction domain = new Transaction(
                UUID.randomUUID(),
                wallet(Currency.BRL, new BigDecimal("100.00")),
                wallet(Currency.USD, new BigDecimal("20.00")),
                new BigDecimal("15.00"),
                TransactionStatus.PENDING,
                TransactionType.REMITTANCE,
                LocalDateTime.of(2026, 3, 1, 9, 0),
                LocalDateTime.of(2026, 3, 1, 9, 1)
        );

        TransactionEntity persisted = transactionEntity(
                domain.id(),
                domain.sourceWallet().id(),
                domain.destinationWallet().id(),
                domain.amount(),
                domain.status(),
                domain.type()
        );

        when(jpaRepository.save(any(TransactionEntity.class))).thenReturn(persisted);

        Transaction saved = repository.save(domain);

        assertNotNull(saved);
        assertEquals(domain.id(), saved.id());
        assertEquals(domain.amount(), saved.amount());
        assertEquals(domain.status(), saved.status());
        assertEquals(domain.type(), saved.type());

        ArgumentCaptor<TransactionEntity> entityCaptor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(jpaRepository).save(entityCaptor.capture());
        assertEquals(domain.id(), entityCaptor.getValue().getId());
        assertEquals(domain.sourceWallet().id(), entityCaptor.getValue().getSourceWallet().getId());
        assertEquals(domain.destinationWallet().id(), entityCaptor.getValue().getDestinationWallet().getId());
    }

    private TransactionEntity transactionEntity(
            UUID txId,
            UUID sourceWalletId,
            UUID destinationWalletId,
            BigDecimal amount,
            TransactionStatus status,
            TransactionType type
    ) {
        return new TransactionEntity(
                txId,
                new WalletEntity(
                        sourceWalletId,
                        Currency.BRL,
                        new BigDecimal("100.00"),
                        LocalDateTime.of(2026, 1, 1, 8, 0),
                        LocalDateTime.of(2026, 1, 1, 8, 5)
                ),
                new WalletEntity(
                        destinationWalletId,
                        Currency.USD,
                        new BigDecimal("20.00"),
                        LocalDateTime.of(2026, 1, 1, 8, 0),
                        LocalDateTime.of(2026, 1, 1, 8, 5)
                ),
                amount,
                status,
                type,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 5)
        );
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

