package com.inter.remittance.application.command;

import com.inter.remittance.domain.enums.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateWithdrawalCommand (
        UUID sourceAccountId,
        Currency sourceCurrency,
        BigDecimal amount
) {}
