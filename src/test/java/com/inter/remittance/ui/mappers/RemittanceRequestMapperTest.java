package com.inter.remittance.ui.mappers;

import com.inter.remittance.application.command.CreateRemittanceCommand;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.ui.requests.CreateRemittanceRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RemittanceRequestMapperTest {

    @Test
    void shouldMapRequestToCommandWithAllFields() {
        UUID sourceId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();

        CreateRemittanceRequest request = new CreateRemittanceRequest(
                sourceId,
                destinationId,
                Currency.BRL,
                Currency.USD,
                new BigDecimal("100.00")
        );

        CreateRemittanceCommand command = RemittanceRequestMapper.toCommand(request);

        assertNotNull(command);
        assertEquals(sourceId, command.sourceAccountId());
        assertEquals(destinationId, command.destinationAccountId());
        assertEquals(Currency.BRL, command.sourceCurrency());
        assertEquals(Currency.USD, command.destinationCurrency());
        assertEquals(new BigDecimal("100.00"), command.amount());
    }

    @Test
    void shouldPreserveAllCurrencyCombinations() {
        CreateRemittanceRequest request = new CreateRemittanceRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                Currency.USD,
                Currency.BRL,
                new BigDecimal("55.50")
        );

        CreateRemittanceCommand command = RemittanceRequestMapper.toCommand(request);

        assertEquals(Currency.USD, command.sourceCurrency());
        assertEquals(Currency.BRL, command.destinationCurrency());
        assertEquals(new BigDecimal("55.50"), command.amount());
    }
}

