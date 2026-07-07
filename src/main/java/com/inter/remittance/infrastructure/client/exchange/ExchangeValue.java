package com.inter.remittance.infrastructure.client.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ExchangeValue(
        @JsonProperty("cotacaoCompra")
        BigDecimal exchangeRate
) {
}
