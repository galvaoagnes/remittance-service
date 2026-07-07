package com.inter.remittance.application.command;

import com.inter.remittance.domain.enums.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateRemittanceCommandTest {

    @Test
    void shouldExposeConstructorValues() {
        UUID sourceId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();

        CreateRemittanceCommand command = new CreateRemittanceCommand(
                sourceId,
                destinationId,
                Currency.BRL,
                Currency.USD,
                new BigDecimal("100.00")
        );

        assertEquals(sourceId, command.sourceAccountId());
        assertEquals(destinationId, command.destinationAccountId());
        assertEquals(Currency.BRL, command.sourceCurrency());
        assertEquals(Currency.USD, command.destinationCurrency());
        assertEquals(0, command.amount().compareTo(new BigDecimal("100.00")));
    }
}

