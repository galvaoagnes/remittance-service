package com.inter.remittance.infrastructure.persistence.repositories;

import com.inter.remittance.domain.entities.DailyTransactionLimit;
import com.inter.remittance.infrastructure.persistence.entities.DailyTransactionLimitEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DailyTransactionLimitJpaRepository extends JpaRepository<DailyTransactionLimitEntity, UUID>{

    DailyTransactionLimitEntity save(DailyTransactionLimit dailyTransactionLimit);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM DailyTransactionLimitEntity d WHERE d.id = :id")
    Optional<DailyTransactionLimitEntity> findByIdWithLock(@Param("id") UUID id);

}
