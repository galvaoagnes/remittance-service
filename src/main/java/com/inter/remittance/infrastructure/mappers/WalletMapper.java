package com.inter.remittance.infrastructure.mappers;

import com.inter.remittance.domain.entities.Remittance;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.infrastructure.persistence.entities.RemittanceEntity;
import com.inter.remittance.infrastructure.persistence.entities.WalletEntity;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public final class WalletMapper {

    private WalletMapper() {
    }

    public static WalletEntity toEntity(Wallet wallet) {
        if (wallet == null) {
            return null;
        }
        return new WalletEntity(
                wallet.id(),
                wallet.currency(),
                wallet.balance(),
                wallet.createdAt(),
                wallet.updatedAt()
                );

    }

    public static Wallet toDomain(WalletEntity entity) {
        if (entity == null) return null;

        return new Wallet(
                entity.getId(),
                entity.getCurrency(),
                entity.getBanlance(),
                Collections.emptySet(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }


    private static Set<Remittance> mapRemittancesToDomain(Set<RemittanceEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptySet();
        }

        return entities.stream()
                .map(RemittanceMapper::toDomain)
                .collect(Collectors.toSet());
    }
}

