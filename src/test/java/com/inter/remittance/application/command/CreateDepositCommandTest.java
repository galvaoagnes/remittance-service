package com.inter.remittance.application.command;

import com.inter.remittance.domain.enums.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateDepositCommandTest {

    @Test
    void shouldExposeConstructorValues() {
        UUID destinationId = UUID.randomUUID();
        CreateDepositCommand command = new CreateDepositCommand(
                destinationId,
                Currency.BRL,
                new BigDecimal("55.10")
        );

        assertEquals(destinationId, command.destinationAccountId());
        assertEquals(Currency.BRL, command.destinationCurrency());
        assertEquals(0, command.amount().compareTo(new BigDecimal("55.10")));
    }
}

