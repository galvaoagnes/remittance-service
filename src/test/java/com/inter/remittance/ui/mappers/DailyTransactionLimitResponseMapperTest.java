package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.DailyTransactionLimit;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.ui.responses.DailyTransactionLimitResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DailyTransactionLimitResponseMapperTest {

    @Test
    void shouldMapDailyTransactionLimitToResponseWithAllFields() {
        UUID id = UUID.randomUUID();
        DailyTransactionLimit limit = new DailyTransactionLimit(
                id,
                Currency.BRL,
                new BigDecimal("10000.00"),
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 5)
        );

        DailyTransactionLimitResponse response = DailyTransactionLimitResponseMapper.toResponse(limit);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals(Currency.BRL, response.currency());
        assertEquals(new BigDecimal("10000.00"), response.amount());
    }

    @Test
    void shouldMapDailyTransactionLimitWithUsdCurrency() {
        DailyTransactionLimit limit = new DailyTransactionLimit(
                UUID.randomUUID(),
                Currency.USD,
                new BigDecimal("30000.00"),
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 5)
        );

        DailyTransactionLimitResponse response = DailyTransactionLimitResponseMapper.toResponse(limit);

        assertNotNull(response);
        assertEquals(Currency.USD, response.currency());
        assertEquals(new BigDecimal("30000.00"), response.amount());
    }

    @Test
    void shouldReturnNullWhenDailyTransactionLimitIsNull() {
        assertNull(DailyTransactionLimitResponseMapper.toResponse(null));
    }
}

