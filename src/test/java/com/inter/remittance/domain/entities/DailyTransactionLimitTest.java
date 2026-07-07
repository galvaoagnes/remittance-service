package com.inter.remittance.domain.entities;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.PersonType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DailyTransactionLimitTest {

    @Test
    void shouldDebitAmountAndUpdateTimestamp() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 1, 1, 10, 0);
        DailyTransactionLimit limit = new DailyTransactionLimit(
                UUID.randomUUID(),
                Currency.BRL,
                new BigDecimal("100.00"),
                createdAt,
                updatedAt
        );

        DailyTransactionLimit debited = limit.debit(new BigDecimal("40.00"));

        assertEquals(0, debited.amount().compareTo(new BigDecimal("60.00")));
        assertEquals(limit.id(), debited.id());
        assertEquals(createdAt, debited.createdAt());
        assertNotNull(debited.updatedAt());
    }

    @Test
    void shouldThrowWhenDebitAmountIsInvalid() {
        DailyTransactionLimit limit = validLimit(new BigDecimal("100.00"));

        BusinessException ex = assertThrows(BusinessException.class, () -> limit.debit(BigDecimal.ZERO));

        assertEquals(ErrorCatalog.INVALID_AMOUNT, ex.getMessage());
    }

    @Test
    void shouldThrowWhenDebitExceedsDailyLimit() {
        DailyTransactionLimit limit = validLimit(new BigDecimal("100.00"));

        BusinessException ex = assertThrows(BusinessException.class, () -> limit.debit(new BigDecimal("100.01")));

        assertEquals(ErrorCatalog.DAILY_LIMIT_EXCEEDED, ex.getMessage());
    }

    @Test
    void shouldCreateInitialDailyLimitsWhenInputIsNull() {
        Set<DailyTransactionLimit> limits = DailyTransactionLimit.setInitialDailyTransactionLimit(null, PersonType.PF);

        assertEquals(1, limits.size());
        DailyTransactionLimit brlLimit = limits.iterator().next();
        assertEquals(Currency.BRL, brlLimit.currency());
        assertEquals(0, brlLimit.amount().compareTo(new BigDecimal("10000")));
    }

    @Test
    void shouldUpdateExistingDailyLimitsBasedOnPersonType() {
        DailyTransactionLimit existing = validLimit(new BigDecimal("500.00"));

        Set<DailyTransactionLimit> updated = DailyTransactionLimit.setInitialDailyTransactionLimit(Set.of(existing), PersonType.PJ);

        assertEquals(1, updated.size());
        DailyTransactionLimit updatedLimit = updated.iterator().next();
        assertEquals(existing.id(), updatedLimit.id());
        assertEquals(Currency.BRL, updatedLimit.currency());
        assertEquals(0, updatedLimit.amount().compareTo(new BigDecimal("30000")));
        assertNotNull(updatedLimit.updatedAt());
    }

    @Test
    void shouldThrowWhenAvailableLimitIsLowerThanRequestedAmount() {
        DailyTransactionLimit limit = validLimit(new BigDecimal("50.00"));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> limit.validateAvailableDailyLimit(new BigDecimal("60.00")));

        assertEquals(ErrorCatalog.DAILY_LIMIT_EXCEEDED, ex.getMessage());
    }

    private DailyTransactionLimit validLimit(BigDecimal amount) {
        return new DailyTransactionLimit(
                UUID.randomUUID(),
                Currency.BRL,
                amount,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 5)
        );
    }
}

