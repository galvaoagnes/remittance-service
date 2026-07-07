package com.inter.remittance.ui.requests;

import com.inter.remittance.domain.enums.Currency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BalanceUpdateRequest(

        @NotNull
        Currency currency,

        @NotNull
        @Positive
        BigDecimal amount
) {
}
