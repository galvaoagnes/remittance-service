package com.inter.remittance.application.command;

import com.inter.remittance.domain.enums.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateRemittanceCommand(
        UUID sourceAccountId,
        UUID destinationAccountId,
        Currency sourceCurrency,
        Currency destinationCurrency,
        BigDecimal amount
) {}