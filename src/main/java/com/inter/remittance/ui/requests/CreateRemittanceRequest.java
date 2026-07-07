package com.inter.remittance.ui.requests;

import com.inter.remittance.domain.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateRemittanceRequest(
        @NotBlank
        UUID sourceAccountId,

        @NotBlank
        UUID destinationAccountId,

        @NotNull
        Currency sourceCurrency,

        @NotNull
        Currency destinationCurrency,

        @NotBlank
        BigDecimal amount
) {
}