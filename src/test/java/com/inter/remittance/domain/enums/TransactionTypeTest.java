package com.inter.remittance.domain.enums;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTypeTest {

    @Test
    void shouldExposeAllTransactionTypes() {
        assertEquals(
                List.of(
                        TransactionType.REMITTANCE,
                        TransactionType.DEPOSIT,
                        TransactionType.WITHDRAWAL,
                        TransactionType.TRANSFER
                ),
                List.of(TransactionType.values())
        );
    }
}

