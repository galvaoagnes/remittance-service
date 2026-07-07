package com.inter.remittance.ui.responses;

import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID walletSourceId,
        UUID walletDestinationId,
        BigDecimal amount,
        TransactionStatus status,
        TransactionType type
) {
}
