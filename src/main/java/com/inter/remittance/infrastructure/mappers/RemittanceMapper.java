package com.inter.remittance.infrastructure.mappers;

import com.inter.remittance.domain.entities.Remittance;
import com.inter.remittance.infrastructure.persistence.entities.RemittanceEntity;

public class RemittanceMapper {

    private RemittanceMapper() {
    }

    public static RemittanceEntity toEntity(Remittance remittance) {
        return new RemittanceEntity(
                remittance.id(),
                TransactionMapper.toEntity(remittance.transaction()),
                remittance.convertedCurrencyAmount(),
                remittance.exchangeRate(),
                remittance.createdAt(),
                remittance.updatedAt()
        );
    }

    public static Remittance toDomain(RemittanceEntity entity) {
        return new Remittance(
                entity.getId(),
                TransactionMapper.toDomain(entity.getTransaction()),
                entity.getConvertedCurrencyAmount(),
                entity.getExchangeRate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
