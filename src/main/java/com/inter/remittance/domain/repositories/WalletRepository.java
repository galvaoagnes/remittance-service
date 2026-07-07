package com.inter.remittance.domain.repositories;

import com.inter.remittance.domain.entities.Wallet;

import java.util.Set;

public interface WalletRepository {
    Wallet save(Wallet wallet);
    Set<Wallet> save(Set<Wallet> wallets);
    default void update(Wallet wallet){}
}
