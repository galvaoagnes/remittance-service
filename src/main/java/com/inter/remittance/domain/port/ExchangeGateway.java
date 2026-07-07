package com.inter.remittance.domain.port;

import com.inter.remittance.domain.enums.Currency;

import java.math.BigDecimal;

public interface ExchangeGateway {
    BigDecimal getExchangeRate(Currency currency);
}
