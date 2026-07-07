package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.ui.responses.PersonResponse;

import java.util.stream.Collectors;

public final class PersonResponseMapper {

    private PersonResponseMapper() {
    }

    public static PersonResponse toResponse(Person person) {
        if (person == null) {
            return null;
        }

        return new PersonResponse(
                    person.id(),
                    person.dailyTransactionLimits().stream().map(
                            DailyTransactionLimitResponseMapper::toResponse
                    ).collect(Collectors.toSet())
        );
    }
}
