package com.inter.remittance.application.command;

import com.inter.remittance.domain.enums.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateWithdrawalCommandTest {

    @Test
    void shouldExposeConstructorValues() {
        UUID sourceId = UUID.randomUUID();
        CreateWithdrawalCommand command = new CreateWithdrawalCommand(
                sourceId,
                Currency.USD,
                new BigDecimal("10.00")
        );

        assertEquals(sourceId, command.sourceAccountId());
        assertEquals(Currency.USD, command.sourceCurrency());
        assertEquals(0, command.amount().compareTo(new BigDecimal("10.00")));
    }
}

