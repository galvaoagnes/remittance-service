package com.inter.remittance.infrastructure.persistence.repositories;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.entities.DailyTransactionLimit;
import com.inter.remittance.domain.repositories.DailyTransactionLimitRepository;
import com.inter.remittance.infrastructure.mappers.DailyTransactionLimitMapper;
import com.inter.remittance.infrastructure.persistence.entities.DailyTransactionLimitEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DailyTransactionLimitDataRepository implements DailyTransactionLimitRepository {
     private final DailyTransactionLimitJpaRepository repository;

    public DailyTransactionLimitDataRepository(DailyTransactionLimitJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public DailyTransactionLimit save(DailyTransactionLimit dailyTransactionLimit) {
        DailyTransactionLimitEntity entity = repository.save(DailyTransactionLimitMapper.toEntity(dailyTransactionLimit));

        return  DailyTransactionLimitMapper.toDomain(entity);
    }

    @Transactional
    @Override
    public void update(DailyTransactionLimit limit) {

        DailyTransactionLimitEntity entity = repository
                .findByIdWithLock(limit.id())
                .orElseThrow(() -> new BusinessException(ErrorCatalog.INVALID_DAILY_TRANSACTION_LIMIT));

        entity.setAmount(limit.amount());
    }


}
