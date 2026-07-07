package com.inter.remittance.application.service;

import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.domain.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    private FindTransactionService service;

    @BeforeEach
    void setUp() {
        service = new FindTransactionService(transactionRepository);
    }

    @Test
    void shouldDelegateFindTransactionsByWalletId() {
        UUID walletId = UUID.randomUUID();
        PageResult<Transaction> expected = new PageResult<>(Set.of(), 0, 10, 0, 0);
        when(transactionRepository.findTransactionsByWalletId(walletId, 0, 10)).thenReturn(expected);

        PageResult<Transaction> result = service.findAllTransactions(walletId, 0, 10);

        assertSame(expected, result);
        verify(transactionRepository).findTransactionsByWalletId(walletId, 0, 10);
    }

    @Test
    void shouldReturnPageResultWithExpectedMetadata() {
        UUID walletId = UUID.randomUUID();
        PageResult<Transaction> expected = new PageResult<>(Set.of(), 2, 25, 100, 4);
        when(transactionRepository.findTransactionsByWalletId(walletId, 2, 25)).thenReturn(expected);

        PageResult<Transaction> result = service.findAllTransactions(walletId, 2, 25);

        assertEquals(2, result.page());
        assertEquals(25, result.size());
        assertEquals(100, result.totalElements());
        assertEquals(4, result.totalPages());
    }
}

