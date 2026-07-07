package com.inter.remittance.application.service;

import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.port.ExchangeGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

@Service
public class UpdateExchangeRateService {

    private final ExchangeGateway exchangeGateway;

    public UpdateExchangeRateService(ExchangeGateway exchangeGateway) {
        this.exchangeGateway = exchangeGateway;
    }

    private static final Logger log = LoggerFactory.getLogger(UpdateExchangeRateService.class);

    @CircuitBreaker(
            name = "exchange-rate",
            fallbackMethod = "fallback"
    )
    @CachePut(value = "exchangeRate", key = "#currency")
    public BigDecimal execute(
            Currency currency
    ) {
        log.info("Updating exchange rate for currency {}", currency);
        BigDecimal exchangeRate = exchangeGateway.getExchangeRate(currency);
        log.info("Exchange rate for currency {} updated to {}", currency, exchangeRate);
        return exchangeRate;
    }

    public BigDecimal fallback(
            Currency currency,
            Throwable ex
    ) {
        throw new RestClientException("Failed to update exchange rate for currency " + currency + ": " +
                "" + ex.getMessage(), ex);
    }
}
