package com.inter.remittance.infrastructure.persistence.repositories;

import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.repositories.WalletRepository;
import com.inter.remittance.infrastructure.mappers.WalletMapper;
import com.inter.remittance.infrastructure.persistence.entities.WalletEntity;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class WalletDataRepository implements WalletRepository {

    private final WalletJpaRepository repository;

    public WalletDataRepository(WalletJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void update(Wallet wallet) {

        WalletEntity entity = repository
                .findByIdWithLock(wallet.id())
                .orElseThrow();

        entity.setBanlance(wallet.balance());
    }

    @Override
    public Wallet save(Wallet wallet) {

        WalletEntity entity = WalletMapper.toEntity(wallet);

        entity = repository.save(entity);

        return WalletMapper.toDomain(entity);
    }

    @Override
    public Set<Wallet> save(Set<Wallet> wallets) {
        return wallets.stream()
                .map(WalletMapper::toEntity)
                .map(repository::save)
                .map(WalletMapper::toDomain)
                .collect(Collectors.toSet());
    }
}
