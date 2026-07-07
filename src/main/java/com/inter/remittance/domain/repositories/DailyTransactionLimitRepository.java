package com.inter.remittance.domain.repositories;

import com.inter.remittance.domain.entities.DailyTransactionLimit;

public interface DailyTransactionLimitRepository {
    DailyTransactionLimit save(DailyTransactionLimit dailyTransactionLimit);

    void update(DailyTransactionLimit limit);
}
