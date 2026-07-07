package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.DailyTransactionLimit;
import com.inter.remittance.ui.responses.DailyTransactionLimitResponse;

public class DailyTransactionLimitResponseMapper {
    private DailyTransactionLimitResponseMapper() {
    }

    public static DailyTransactionLimitResponse toResponse(DailyTransactionLimit dailyTransactionLimit) {
        if (dailyTransactionLimit == null) {
            return null;
        }

        return new DailyTransactionLimitResponse(
                dailyTransactionLimit.id(),
                dailyTransactionLimit.currency(),
                dailyTransactionLimit.amount()
        );
    }
}
