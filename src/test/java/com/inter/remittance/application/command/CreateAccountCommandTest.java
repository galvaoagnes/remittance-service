package com.inter.remittance.application.command;

import com.inter.remittance.domain.enums.PersonType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateAccountCommandTest {

    @Test
    void shouldExposeConstructorValues() {
        CreateAccountCommand command = new CreateAccountCommand(
                "Agnes",
                "Galvao",
                PersonType.PF,
                "12345678901",
                "Valid@123",
                "agnes@example.com"
        );

        assertEquals("Agnes", command.name());
        assertEquals("Galvao", command.lastName());
        assertEquals(PersonType.PF, command.personType());
        assertEquals("12345678901", command.documentNumber());
        assertEquals("Valid@123", command.password());
        assertEquals("agnes@example.com", command.email());
    }
}

