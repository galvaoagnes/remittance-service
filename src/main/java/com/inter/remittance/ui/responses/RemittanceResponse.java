package com.inter.remittance.ui.responses;

import com.inter.remittance.domain.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record RemittanceResponse(
        UUID remittanceId,
        UUID transactionId,
        BigDecimal originalAmount,
        BigDecimal convertedAmount,
        BigDecimal exchangeRate,
        TransactionStatus transactionStatus,
        LocalDateTime createdAt
) {
}