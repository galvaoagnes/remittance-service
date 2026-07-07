package com.inter.remittance.domain.strategies;


import com.inter.remittance.domain.enums.Currency;

import java.math.BigDecimal;

public interface CurrencyConversionStrategy {
    Currency source();

    Currency target();

    BigDecimal convert(BigDecimal amount, BigDecimal exchangeRate);
}
