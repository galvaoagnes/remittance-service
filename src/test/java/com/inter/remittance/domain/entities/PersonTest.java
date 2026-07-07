package com.inter.remittance.domain.entities;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.domain.valueobjects.Document;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonTest {

    @Test
    void shouldThrowWhenNameIsNull() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> new Person(
                        UUID.randomUUID(),
                        null,
                        "Galvao",
                        new Document(PersonType.PF, "12345678901"),
                        "agnes@example.com",
                        "Valid@123",
                        null,
                        null,
                        null
                )
        );

        assertEquals(ErrorCatalog.INVALID_NAME, ex.getMessage());
    }

    @Test
    void shouldThrowWhenLastNameIsBlank() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> new Person(
                        UUID.randomUUID(),
                        "Agnes",
                        "   ",
                        new Document(PersonType.PF, "12345678901"),
                        "agnes@example.com",
                        "Valid@123",
                        null,
                        null,
                        null
                )
        );

        assertEquals(ErrorCatalog.INVALID_LAST_NAME, ex.getMessage());
    }

    @Test
    void shouldThrowWhenDocumentIsNull() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> new Person(
                        UUID.randomUUID(),
                        "Agnes",
                        "Galvao",
                        null,
                        "agnes@example.com",
                        "Valid@123",
                        null,
                        null,
                        null
                )
        );

        assertEquals(ErrorCatalog.INVALID_DOCUMENT, ex.getMessage());
    }

    @Test
    void shouldThrowWhenEmailIsInvalid() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> new Person(
                        UUID.randomUUID(),
                        "Agnes",
                        "Galvao",
                        new Document(PersonType.PF, "12345678901"),
                        "invalid-email",
                        "Valid@123",
                        null,
                        null,
                        null
                )
        );

        assertEquals(ErrorCatalog.INVALID_EMAIL, ex.getMessage());
    }

    @Test
    void shouldGenerateIdAndDefaultFieldsWhenNull() {
        Person person = new Person(
                null,
                "Agnes",
                "Galvao",
                new Document(PersonType.PF, "12345678901"),
                "agnes@example.com",
                "Valid@123",
                null,
                null,
                null
        );

        assertNotNull(person.id());
        assertNotNull(person.createdAt());
        assertNotNull(person.updatedAt());
        assertNotNull(person.dailyTransactionLimits());
        assertEquals(1, person.dailyTransactionLimits().size());
        DailyTransactionLimit brlLimit = person.dailyTransactionLimits().iterator().next();
        assertEquals(Currency.BRL, brlLimit.currency());
        assertEquals(0, brlLimit.amount().compareTo(new BigDecimal("10000")));
    }

    @Test
    void shouldKeepProvidedDailyLimitsAndTimestamps() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 1, 2, 10, 0);
        Set<DailyTransactionLimit> limits = Set.of(
                new DailyTransactionLimit(
                        UUID.randomUUID(),
                        Currency.BRL,
                        new BigDecimal("999"),
                        createdAt,
                        updatedAt
                )
        );

        Person person = new Person(
                UUID.randomUUID(),
                "Agnes",
                "Galvao",
                new Document(PersonType.PF, "12345678901"),
                "agnes@example.com",
                "Valid@123",
                limits,
                createdAt,
                updatedAt
        );

        assertSame(limits, person.dailyTransactionLimits());
        assertEquals(createdAt, person.createdAt());
        assertEquals(updatedAt, person.updatedAt());
    }

    @Test
    void shouldCreateNewPersonWithPfInitialLimit() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 1, 9, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 6, 1, 9, 5);

        Person person = Person.createNewPerson(
                "Agnes",
                "Galvao",
                "12345678901",
                PersonType.PF,
                "Valid@123",
                "agnes@example.com",
                createdAt,
                updatedAt
        );

        assertNotNull(person.id());
        assertEquals(createdAt, person.createdAt());
        assertEquals(updatedAt, person.updatedAt());
        assertEquals(1, person.dailyTransactionLimits().size());
        DailyTransactionLimit brlLimit = person.dailyTransactionLimits().iterator().next();
        assertEquals(Currency.BRL, brlLimit.currency());
        assertEquals(0, brlLimit.amount().compareTo(new BigDecimal("10000")));
    }

    @Test
    void shouldCreateNewPersonWithPjInitialLimit() {
        Person person = Person.createNewPerson(
                "Empresa",
                "LTDA",
                "12345678000199",
                PersonType.PJ,
                "Valid@123",
                "empresa@example.com",
                null,
                null
        );

        assertNotNull(person.createdAt());
        assertNotNull(person.updatedAt());
        DailyTransactionLimit brlLimit = person.dailyTransactionLimits().iterator().next();
        assertEquals(0, brlLimit.amount().compareTo(new BigDecimal("30000")));
    }

    @Test
    void shouldThrowWhenCreateNewPersonPasswordIsInvalid() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> Person.createNewPerson(
                        "Agnes",
                        "Galvao",
                        "12345678901",
                        PersonType.PF,
                        "weak",
                        "agnes@example.com",
                        null,
                        null
                )
        );

        assertEquals(ErrorCatalog.INVALID_PASSWORD, ex.getMessage());
    }

    @Test
    void shouldThrowWhenCreateNewPersonDocumentDoesNotMatchType() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> Person.createNewPerson(
                        "Empresa",
                        "LTDA",
                        "12345678901",
                        PersonType.PJ,
                        "Valid@123",
                        "empresa@example.com",
                        null,
                        null
                )
        );

        assertEquals(ErrorCatalog.INVALID_CNPJ_NUMBER, ex.getMessage());
    }

    @Test
    void shouldThrowWhenCreateNewPersonEmailIsInvalid() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> Person.createNewPerson(
                        "Agnes",
                        "Galvao",
                        "12345678901",
                        PersonType.PF,
                        "Valid@123",
                        "invalid",
                        null,
                        null
                )
        );

        assertEquals(ErrorCatalog.INVALID_EMAIL, ex.getMessage());
    }

    @Test
    void shouldThrowWhenCreateNewPersonNameIsBlank() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> Person.createNewPerson(
                        " ",
                        "Galvao",
                        "12345678901",
                        PersonType.PF,
                        "Valid@123",
                        "agnes@example.com",
                        null,
                        null
                )
        );

        assertEquals(ErrorCatalog.INVALID_NAME, ex.getMessage());
    }

    @Test
    void shouldThrowWhenCreateNewPersonLastNameIsBlank() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> Person.createNewPerson(
                        "Agnes",
                        " ",
                        "12345678901",
                        PersonType.PF,
                        "Valid@123",
                        "agnes@example.com",
                        null,
                        null
                )
        );

        assertEquals(ErrorCatalog.INVALID_LAST_NAME, ex.getMessage());
    }
}


