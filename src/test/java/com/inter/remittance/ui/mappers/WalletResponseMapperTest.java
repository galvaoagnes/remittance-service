package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.ui.responses.WalletCreatedResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WalletResponseMapperTest {

    @Test
    void shouldMapWalletToResponseWithAllFields() {
        Wallet wallet = new Wallet(
                UUID.randomUUID(),
                Currency.BRL,
                new BigDecimal("250.00"),
                Set.of(),
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 5)
        );

        WalletCreatedResponse response = WalletResponseMapper.toResponse(wallet);

        assertNotNull(response);
        assertEquals(wallet.id(), response.id());
        assertEquals(Currency.BRL, response.currency());
        assertEquals(new BigDecimal("250.00"), response.balance());
    }

    @Test
    void shouldMapWalletWithZeroBalance() {
        Wallet wallet = new Wallet(
                UUID.randomUUID(),
                Currency.USD,
                BigDecimal.ZERO,
                Set.of(),
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 5)
        );

        WalletCreatedResponse response = WalletResponseMapper.toResponse(wallet);

        assertNotNull(response);
        assertEquals(Currency.USD, response.currency());
        assertEquals(BigDecimal.ZERO, response.balance());
    }

    @Test
    void shouldReturnNullWhenWalletIsNull() {
        assertNull(WalletResponseMapper.toResponse(null));
    }
}

