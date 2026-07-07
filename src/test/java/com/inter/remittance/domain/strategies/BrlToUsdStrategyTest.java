package com.inter.remittance.domain.strategies;

import com.inter.remittance.domain.enums.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BrlToUsdStrategyTest {

    private final BrlToUsdStrategy strategy = new BrlToUsdStrategy();

    @Test
    void shouldExposeSourceAndTargetCurrencies() {
        assertEquals(Currency.BRL, strategy.source());
        assertEquals(Currency.USD, strategy.target());
    }

    @Test
    void shouldConvertBrlToUsdWithHalfUpRounding() {
        BigDecimal converted = strategy.convert(new BigDecimal("10.00"), new BigDecimal("3.00"));

        assertEquals(0, converted.compareTo(new BigDecimal("3.33")));
    }
}

