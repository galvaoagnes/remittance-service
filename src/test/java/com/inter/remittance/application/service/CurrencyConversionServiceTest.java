package com.inter.remittance.application.service;

import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.strategies.BrlToUsdStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrencyConversionServiceTest {

    private CurrencyConversionService service;

    @BeforeEach
    void setUp() {
        service = new CurrencyConversionService(List.of(new BrlToUsdStrategy()));
    }

    @Test
    void shouldConvertUsingRegisteredStrategy() {
        BigDecimal converted = service.convert(
                new BigDecimal("10.00"),
                Currency.BRL,
                Currency.USD,
                new BigDecimal("2.00")
        );

        assertEquals(0, converted.compareTo(new BigDecimal("5.00")));
    }

    @Test
    void shouldThrowWhenStrategyDoesNotExist() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.convert(
                        new BigDecimal("10.00"),
                        Currency.USD,
                        Currency.BRL,
                        new BigDecimal("2.00")
                )
        );

        assertEquals("", ex.getMessage());
    }
}

