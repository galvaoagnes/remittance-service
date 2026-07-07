package com.inter.remittance.domain.entities;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RemittanceTest {

    @Test
    void shouldGenerateIdWhenNullInConstructor() {
        Remittance remittance = new Remittance(
                null,
                null,
                new BigDecimal("10.00"),
                new BigDecimal("5.00"),
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 5)
        );

        assertNotNull(remittance.id());
    }

    @Test
    void shouldThrowWhenConvertedCurrencyAmountIsInvalid() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> new Remittance(
                        UUID.randomUUID(),
                        null,
                        new BigDecimal("-1.00"),
                        new BigDecimal("5.00"),
                        LocalDateTime.of(2026, 1, 1, 10, 0),
                        LocalDateTime.of(2026, 1, 1, 10, 5)
                )
        );

        assertEquals(ErrorCatalog.INVALID_ORIGINAL_CURRENCY_AMOUNT, ex.getMessage());
    }

    @Test
    void shouldThrowWhenExchangeRateIsInvalid() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> new Remittance(
                        UUID.randomUUID(),
                        null,
                        new BigDecimal("1.00"),
                        BigDecimal.ZERO,
                        LocalDateTime.of(2026, 1, 1, 10, 0),
                        LocalDateTime.of(2026, 1, 1, 10, 5)
                )
        );

        assertEquals(ErrorCatalog.INVALID_EXCHANGE_RATE, ex.getMessage());
    }

    @Test
    void shouldThrowWhenCreatedAtIsNull() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> new Remittance(
                        UUID.randomUUID(),
                        null,
                        new BigDecimal("1.00"),
                        new BigDecimal("5.00"),
                        null,
                        LocalDateTime.of(2026, 1, 1, 10, 5)
                )
        );

        assertEquals(ErrorCatalog.INVALID_CREATED_AT, ex.getMessage());
    }

    @Test
    void shouldThrowWhenUpdatedAtIsNull() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> new Remittance(
                        UUID.randomUUID(),
                        null,
                        new BigDecimal("1.00"),
                        new BigDecimal("5.00"),
                        LocalDateTime.of(2026, 1, 1, 10, 0),
                        null
                )
        );

        assertEquals(ErrorCatalog.INVALID_UPDATED_AT, ex.getMessage());
    }

    @Test
    void shouldCreateNewRemittanceUsingFactoryMethod() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 1, 1, 10, 5);

        Remittance remittance = Remittance.createNew(
                null,
                new BigDecimal("5.00"),
                new BigDecimal("100.00"),
                createdAt,
                updatedAt
        );

        assertNotNull(remittance.id());
        assertEquals(0, remittance.convertedCurrencyAmount().compareTo(new BigDecimal("100.00")));
        assertEquals(0, remittance.exchangeRate().compareTo(new BigDecimal("5.00")));
        assertEquals(createdAt, remittance.createdAt());
        assertEquals(updatedAt, remittance.updatedAt());
    }

    @Test
    void shouldThrowWhenCreateNewHasInvalidExchangeRate() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> Remittance.createNew(
                        null,
                        BigDecimal.ZERO,
                        new BigDecimal("100.00"),
                        LocalDateTime.of(2026, 1, 1, 10, 0),
                        LocalDateTime.of(2026, 1, 1, 10, 5)
                )
        );

        assertEquals(ErrorCatalog.INVALID_EXCHANGE_RATE, ex.getMessage());
    }
}

