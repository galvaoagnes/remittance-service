package com.inter.remittance.ui.responses;

import com.inter.remittance.domain.enums.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public record DailyTransactionLimitResponse(
        UUID id,
        Currency currency,
        BigDecimal amount
) {
}
