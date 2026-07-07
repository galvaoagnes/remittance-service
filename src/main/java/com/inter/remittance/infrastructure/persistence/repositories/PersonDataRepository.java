package com.inter.remittance.infrastructure.persistence.repositories;

import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.repositories.PersonRepository;
import com.inter.remittance.infrastructure.mappers.PersonMapper;
import com.inter.remittance.infrastructure.persistence.entities.PersonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PersonDataRepository implements PersonRepository {

    private final PersonJpaRepository repository;

    public PersonDataRepository(PersonJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Person save(Person person) {

        PersonEntity entity = PersonMapper.toEntity(person);

        entity = repository.save(entity);

        return PersonMapper.toDomain(entity);
    }

    @Override
    public boolean existsByDocumentValue(String document) {
        return repository.existsByDocumentValue(document);
    }

    @Override
    public Set<Person> findAll() {
        return repository
                .findAll()
                .stream()
                .map(PersonMapper::toDomain)
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public PageResult<Person> findAll(int page, int size) {
        Page<PersonEntity> result =
                repository.findAll(
                        PageRequest.of(page, size)
                );

        return  new PageResult<>(result.stream()
                .map(PersonMapper::toDomain)
                .collect(Collectors.toSet()),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public Person findByDocumentValue(String email) {
        return PersonMapper.toDomain(repository.findByDocumentValue(email));
    }
}
