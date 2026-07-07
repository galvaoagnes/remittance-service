package com.inter.remittance.domain.strategies;

import com.inter.remittance.domain.enums.Currency;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class BrlToUsdStrategy implements CurrencyConversionStrategy {

    @Override
    public Currency source() {
        return Currency.BRL;
    }

    @Override
    public Currency target() {
        return Currency.USD;
    }

    @Override
    public BigDecimal convert(BigDecimal amount, BigDecimal exchangeRate) {
        return amount.divide(exchangeRate, 2, RoundingMode.HALF_UP);
    }
}