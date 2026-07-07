package com.inter.remittance.ui.responses;

import java.util.Set;
import java.util.UUID;

public record PersonResponse(
        UUID id,
        Set<DailyTransactionLimitResponse> dailyTransactionLimitsResponse
) {}
