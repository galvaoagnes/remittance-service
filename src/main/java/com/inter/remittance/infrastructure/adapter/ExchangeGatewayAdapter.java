package com.inter.remittance.infrastructure.adapter;

import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.port.ExchangeGateway;
import com.inter.remittance.infrastructure.client.exchange.ExchangeClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ExchangeGatewayAdapter implements ExchangeGateway {

    private final ExchangeClient client;

    public ExchangeGatewayAdapter(ExchangeClient client) {
        this.client = client;
    }

    @Override
    public BigDecimal getExchangeRate(Currency currency) {
        if (currency.equals(Currency.USD)) {
            return client.getExchangeRateForUSD().value().get(0).exchangeRate();
        }

        throw new IllegalArgumentException("Invalid currency");
    }
}
