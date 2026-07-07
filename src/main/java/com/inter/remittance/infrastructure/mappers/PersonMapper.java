package com.inter.remittance.infrastructure.mappers;

import com.inter.remittance.domain.valueobjects.Document;
import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.infrastructure.persistence.entities.DocumentEmbeddable;
import com.inter.remittance.infrastructure.persistence.entities.PersonEntity;

import java.util.stream.Collectors;

public final class PersonMapper {

    private PersonMapper() {
    }

    public static PersonEntity toEntity(Person person) {
      PersonEntity entity = new PersonEntity(
              person.id(),
              new DocumentEmbeddable(
                      person.document().type(),
                      person.document().value()
              ),
                person.name(),
                person.lastName(),
                person.email(),
                person.password(),
                person.createdAt(),
                person.updatedAt()
              );
        person.dailyTransactionLimits().stream()
                .map(DailyTransactionLimitMapper::toEntity)
                .forEach(entity::addDailyTransactionLimit);

        return entity;
    }

    public static Person toDomain(PersonEntity entity) {
        return new Person(
                entity.getId(),
                entity.getName(),
                entity.getLastName(),
                new Document(
                        entity.getDocument().getType(),
                        entity.getDocument().getValue()
                ),
                entity.getEmail(),
                entity.getPassword(),
                entity.getDailyTransactionLimits()
                        .stream()
                        .map(DailyTransactionLimitMapper::toDomain)
                        .collect(Collectors.toSet()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}