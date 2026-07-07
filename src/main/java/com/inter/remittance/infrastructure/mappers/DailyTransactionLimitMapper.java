package com.inter.remittance.infrastructure.mappers;

import com.inter.remittance.domain.entities.DailyTransactionLimit;
import com.inter.remittance.infrastructure.persistence.entities.DailyTransactionLimitEntity;

public final class DailyTransactionLimitMapper {

    private DailyTransactionLimitMapper() {
    }

    public static DailyTransactionLimitEntity toEntity(DailyTransactionLimit dailyTransactionLimit){
        return new DailyTransactionLimitEntity(
                dailyTransactionLimit.id(),
                dailyTransactionLimit.currency(),
                dailyTransactionLimit.amount(),
                dailyTransactionLimit.createdAt(),
                dailyTransactionLimit.updatedAt()
        );
    }

    public static DailyTransactionLimit toDomain(
            DailyTransactionLimitEntity entity
    ){
        return new DailyTransactionLimit(
                entity.getId(),
                entity.getCurrency(),
                entity.getAmount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
