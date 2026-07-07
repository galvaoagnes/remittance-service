package com.inter.remittance.domain.repositories;

import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.entities.Person;

import java.util.Set;

public interface PersonRepository {
    Person save(Person person);

    boolean existsByDocumentValue(String document);

    Set<Person> findAll();

    PageResult<Person> findAll(int page, int size);

    Person findByDocumentValue(String document);
}
