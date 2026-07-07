package com.inter.remittance.infrastructure.persistence.repositories;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.entities.Remittance;
import com.inter.remittance.domain.repositories.RemittanceRepository;
import com.inter.remittance.infrastructure.mappers.RemittanceMapper;
import com.inter.remittance.infrastructure.persistence.entities.RemittanceEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RemittanceDataRepository implements RemittanceRepository {
    private final RemittanceJpaRepository repository;

    public RemittanceDataRepository(RemittanceJpaRepository remittanceJpaRepository) {
        this.repository = remittanceJpaRepository;
    }

    @Override
    public Remittance save(Remittance remittance, UUID sourceWalletId) {
        RemittanceEntity entity = repository.save(RemittanceMapper.toEntity(remittance));
        return RemittanceMapper.toDomain(entity);
    }

    @Override
    public Remittance findById(UUID id) {
        return RemittanceMapper.toDomain(
                repository.findById(id).orElseThrow(
                        () -> new BusinessException(ErrorCatalog.REMITTANCE_NOT_FOUND)
                )
        );
    }
}
