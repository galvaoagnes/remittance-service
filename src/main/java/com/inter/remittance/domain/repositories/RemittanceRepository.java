package com.inter.remittance.domain.repositories;

import com.inter.remittance.domain.entities.Remittance;

import java.util.UUID;

public interface RemittanceRepository {

    Remittance save(Remittance remittance, UUID sourceWalletId);
    Remittance findById(UUID id);
}
