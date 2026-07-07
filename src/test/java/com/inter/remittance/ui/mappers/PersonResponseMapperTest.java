package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.DailyTransactionLimit;
import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.domain.valueobjects.Document;
import com.inter.remittance.ui.responses.PersonResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PersonResponseMapperTest {

    @Test
    void shouldMapPersonToResponseWithDailyTransactionLimits() {
        DailyTransactionLimit limit = new DailyTransactionLimit(
                UUID.randomUUID(),
                Currency.BRL,
                new BigDecimal("10000.00"),
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 0)
        );

        UUID personId = UUID.randomUUID();
        Person person = new Person(
                personId,
                "Agnes",
                "Galvao",
                new Document(PersonType.PF, "12345678901"),
                "agnes@example.com",
                "Valid@123",
                Set.of(limit),
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 0)
        );

        PersonResponse response = PersonResponseMapper.toResponse(person);

        assertNotNull(response);
        assertEquals(personId, response.id());
        assertEquals(1, response.dailyTransactionLimitsResponse().size());

        var limitResponse = response.dailyTransactionLimitsResponse().iterator().next();
        assertEquals(limit.id(), limitResponse.id());
        assertEquals(Currency.BRL, limitResponse.currency());
        assertEquals(new BigDecimal("10000.00"), limitResponse.amount());
    }

    @Test
    void shouldMapPersonWithMultipleDailyTransactionLimits() {
        DailyTransactionLimit brlLimit = new DailyTransactionLimit(
                UUID.randomUUID(), Currency.BRL, new BigDecimal("10000.00"),
                LocalDateTime.of(2026, 1, 1, 8, 0), LocalDateTime.of(2026, 1, 1, 8, 0)
        );
        DailyTransactionLimit usdLimit = new DailyTransactionLimit(
                UUID.randomUUID(), Currency.USD, new BigDecimal("2000.00"),
                LocalDateTime.of(2026, 1, 1, 8, 0), LocalDateTime.of(2026, 1, 1, 8, 0)
        );

        Person person = new Person(
                UUID.randomUUID(),
                "Agnes",
                "Galvao",
                new Document(PersonType.PF, "12345678901"),
                "agnes@example.com",
                "Valid@123",
                Set.of(brlLimit, usdLimit),
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 0)
        );

        PersonResponse response = PersonResponseMapper.toResponse(person);

        assertNotNull(response);
        assertEquals(2, response.dailyTransactionLimitsResponse().size());
    }

    @Test
    void shouldReturnNullWhenPersonIsNull() {
        assertNull(PersonResponseMapper.toResponse(null));
    }
}

