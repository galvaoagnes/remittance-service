package com.inter.remittance.application.service;

import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.strategies.CurrencyConversionStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CurrencyConversionService {

    private final Map<String, CurrencyConversionStrategy> strategies;

    public CurrencyConversionService(List<CurrencyConversionStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(
                        s -> key(s.source(), s.target()),
                        Function.identity()
                ));
    }

    public BigDecimal convert(
            BigDecimal amount,
            Currency from,
            Currency to,
            BigDecimal exchangeRate) {

        CurrencyConversionStrategy strategy =
                strategies.get(key(from, to));

        if (strategy == null) {
            throw  new BusinessException("");
        }

        return strategy.convert(amount, exchangeRate);
    }

    private String key(Currency from, Currency to) {
        return from + ":" + to;
    }
}