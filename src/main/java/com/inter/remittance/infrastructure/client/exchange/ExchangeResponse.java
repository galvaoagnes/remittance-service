package com.inter.remittance.infrastructure.client.exchange;

import java.util.List;

public record ExchangeResponse(
        List<ExchangeValue> value
) {
}

