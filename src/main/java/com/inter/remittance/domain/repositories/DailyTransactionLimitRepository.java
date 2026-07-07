package com.inter.remittance.domain.repositories;

import com.inter.remittance.domain.entities.DailyTransactionLimit;
import org.springframework.transaction.annotation.Transactional;

public interface DailyTransactionLimitRepository {
    DailyTransactionLimit save(DailyTransactionLimit dailyTransactionLimit);

    @Transactional
    void update(DailyTransactionLimit limit);
}
