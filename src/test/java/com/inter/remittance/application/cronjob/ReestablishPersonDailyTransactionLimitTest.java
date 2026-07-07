package com.inter.remittance.application.cronjob;

import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.domain.repositories.PersonRepository;
import com.inter.remittance.domain.valueobjects.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReestablishPersonDailyTransactionLimitTest {

    @Mock
    private PersonRepository personRepository;

    private ReestablishPersonDailyTransactionLimit job;

    @BeforeEach
    void setUp() {
        job = new ReestablishPersonDailyTransactionLimit(personRepository);
    }

    @Test
    void shouldIterateOverPeopleWithoutThrowing() {
        PageResult<Person> peoplePage = new PageResult<>(Set.of(validPerson()), 0, 100, 1, 1);
        when(personRepository.findAll(anyInt(), anyInt())).thenReturn(peoplePage);

        assertDoesNotThrow(() -> job.execute());

        verify(personRepository).findAll(0, 100);
    }

    private Person validPerson() {
        return new Person(
                UUID.randomUUID(),
                "Agnes",
                "Galvao",
                new Document(PersonType.PF, "12345678901"),
                "agnes@example.com",
                "Valid@123",
                null,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 5)
        );
    }
}

