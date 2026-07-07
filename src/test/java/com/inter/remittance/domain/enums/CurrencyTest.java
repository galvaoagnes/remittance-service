package com.inter.remittance.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CurrencyTest {

    @Test
    void shouldExposeExpectedFlagsForBrl() {
        assertFalse(Currency.BRL.hasExchangeRate());
        assertTrue(Currency.BRL.hasDailyTransactionLimit());
    }

    @Test
    void shouldExposeExpectedFlagsForUsd() {
        assertTrue(Currency.USD.hasExchangeRate());
        assertFalse(Currency.USD.hasDailyTransactionLimit());
    }
}

