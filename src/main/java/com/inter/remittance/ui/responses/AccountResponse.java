package com.inter.remittance.ui.responses;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record AccountResponse (
        UUID id,
        PersonResponse person,
        Set<WalletCreatedResponse> walletCreatedResponses,
        boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){
}
