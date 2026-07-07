package com.inter.remittance.infrastructure.persistence.repositories;


import com.inter.remittance.infrastructure.persistence.entities.AccountEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {

    @EntityGraph(attributePaths = {"personEntity", "wallets"})
    AccountEntity findWithDetailsById(UUID id);
}
