package com.inter.remittance.infrastructure.persistence.repositories;


import com.inter.remittance.infrastructure.persistence.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PersonJpaRepository extends JpaRepository<PersonEntity, UUID> {

    @Query(
     """
     select p from PersonEntity p
     left join fetch p.dailyTransactionLimits
     where p.documentEmbeddable.value = :value
     """
    )
    PersonEntity findByDocumentValue(String value);

    @Query("select (count(p) > 0) from PersonEntity p where p.documentEmbeddable.value = :value")
    boolean existsByDocumentValue(@Param("value") String value);
}
