package com.inter.remittance.infrastructure.persistence.repositories;


import com.inter.remittance.infrastructure.persistence.entities.RemittanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RemittanceJpaRepository extends JpaRepository<RemittanceEntity, UUID> {}