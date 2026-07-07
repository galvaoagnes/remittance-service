package com.inter.remittance.domain.entities;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record Account(
    UUID id,
    Person person,
    Set<Wallet> wallets,
    Boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

){
    public Account {
        id = (id == null) ? UUID.randomUUID() : id;
        validatePerson(person);
        validateWallets(wallets);
        isActive = isActive == null || isActive;
    }

    private static void validatePerson(Person person) {
        if (person == null) {
            throw new BusinessException(ErrorCatalog.INVALID_ACCOUNT_PERSON);
        }
    }

    private static void validateWallets(Set<Wallet> wallets) {
        if (wallets == null) {
            throw new BusinessException(ErrorCatalog.INVALID_ACCOUNT_WALLETS);
        }
    }


    public Account addWallet(Wallet wallet) {
        if (wallet == null) {
            throw new BusinessException(ErrorCatalog.INVALID_ACCOUNT_WALLET);
        }

        boolean alreadyExists = wallets.stream()
                .anyMatch(existing -> existing.currency().equals(wallet.currency()));

        if (alreadyExists) {
            throw new BusinessException(ErrorCatalog.INVALID_CURRENCY_WALLET);
        }

        Set<Wallet> updatedWallets = new HashSet<>(this.wallets);
        updatedWallets.add(wallet);

        return new Account(
                this.id,
                this.person,
                updatedWallets,
                this.isActive,
                this.createdAt,
                LocalDateTime.now()
        );
    }
}
