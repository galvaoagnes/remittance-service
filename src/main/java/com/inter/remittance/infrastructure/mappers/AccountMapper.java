package com.inter.remittance.infrastructure.mappers;

import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.infrastructure.persistence.entities.AccountEntity;

import java.util.Set;
import java.util.stream.Collectors;

public final class AccountMapper {

    private AccountMapper() {
    }

    public static AccountEntity toEntity(Account account) {

        AccountEntity entity = new AccountEntity();

        entity.setId(account.id());
        entity.setPerson(PersonMapper.toEntity(account.person()));

        account.wallets().stream()
                .map(WalletMapper::toEntity)
                .forEach(entity::addWallet);

        entity.seIsActive(account.isActive());
        entity.setCreatedAt(account.createdAt());
        entity.setUpdatedAt(account.updatedAt());

        return entity;
    }

    public static Account toDomain(AccountEntity entity) {
        Set<Wallet> wallets = entity.getWallets().stream()
                .map(WalletMapper::toDomain)
                .collect(Collectors.toSet());

        return new Account(
                entity.getId(),
                PersonMapper.toDomain(entity.getPerson()),
                wallets,
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
