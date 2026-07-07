package com.inter.remittance.ui.mappers;

import com.inter.remittance.application.command.CreateAccountCommand;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.ui.requests.CreateAccountRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountRequestMapperTest {

    @Test
    void shouldMapRequestToCommandWithAllFields() {
        CreateAccountRequest request = new CreateAccountRequest(
                "Agnes",
                "Galvao",
                PersonType.PF,
                "12345678901",
                "Valid@123",
                "agnes@example.com"
        );

        CreateAccountCommand command = AccountRequestMapper.toCommand(request);

        assertNotNull(command);
        assertEquals("Agnes", command.name());
        assertEquals("Galvao", command.lastName());
        assertEquals(PersonType.PF, command.personType());
        assertEquals("12345678901", command.documentNumber());
        assertEquals("Valid@123", command.password());
        assertEquals("agnes@example.com", command.email());
    }

    @Test
    void shouldMapRequestToCommandForPJPersonType() {
        CreateAccountRequest request = new CreateAccountRequest(
                "Empresa",
                "LTDA",
                PersonType.PJ,
                "12345678000195",
                "Senha@1234",
                "empresa@example.com"
        );

        CreateAccountCommand command = AccountRequestMapper.toCommand(request);

        assertNotNull(command);
        assertEquals(PersonType.PJ, command.personType());
        assertEquals("12345678000195", command.documentNumber());
        assertEquals("empresa@example.com", command.email());
    }

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        assertNull(AccountRequestMapper.toCommand(null));
    }
}

