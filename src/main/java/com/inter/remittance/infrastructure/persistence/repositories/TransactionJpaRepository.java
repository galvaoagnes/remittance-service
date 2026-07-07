package com.inter.remittance.infrastructure.persistence.repositories;

import com.inter.remittance.infrastructure.persistence.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {
    @Query("""
       select t
       from TransactionEntity t
       where t.sourceWallet.id = :walletId
          or t.destinationWallet.id = :walletId
       order by t.createdAt desc
       """)
    Page<TransactionEntity> findByWalletId(
            @Param("walletId") UUID walletId,
            Pageable pageable
    );
}
