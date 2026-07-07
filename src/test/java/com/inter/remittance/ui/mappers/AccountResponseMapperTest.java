package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.DailyTransactionLimit;
import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.domain.valueobjects.Document;
import com.inter.remittance.ui.responses.AccountResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountResponseMapperTest {

    @Test
    void shouldMapAccountToResponseWithPersonAndWallets() {
        Wallet brlWallet = wallet(Currency.BRL, new BigDecimal("500.00"));
        Wallet usdWallet = wallet(Currency.USD, new BigDecimal("50.00"));

        UUID accountId = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.of(2026, 1, 1, 8, 0);
        LocalDateTime updated = LocalDateTime.of(2026, 1, 1, 9, 0);

        Account account = new Account(
                accountId,
                person(),
                Set.of(brlWallet, usdWallet),
                true,
                created,
                updated
        );

        AccountResponse response = AccountResponseMapper.toResponse(account);

        assertNotNull(response);
        assertEquals(accountId, response.id());
        assertNotNull(response.person());
        assertEquals(account.person().id(), response.person().id());
        assertTrue(response.isActive());
        assertEquals(created, response.createdAt());
        assertEquals(updated, response.updatedAt());
        assertEquals(2, response.walletCreatedResponses().size());
        assertTrue(response.walletCreatedResponses().stream()
                .anyMatch(w -> w.currency() == Currency.BRL && w.balance().compareTo(new BigDecimal("500.00")) == 0));
        assertTrue(response.walletCreatedResponses().stream()
                .anyMatch(w -> w.currency() == Currency.USD && w.balance().compareTo(new BigDecimal("50.00")) == 0));
    }

    @Test
    void shouldMapAccountToResponseWithEmptyWallets() {
        Account account = new Account(
                UUID.randomUUID(),
                person(),
                Set.of(),
                true,
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 0)
        );

        AccountResponse response = AccountResponseMapper.toResponse(account);

        assertNotNull(response);
        assertTrue(response.walletCreatedResponses().isEmpty());
    }

    @Test
    void shouldMapInactiveAccount() {
        Account account = new Account(
                UUID.randomUUID(),
                person(),
                Set.of(wallet(Currency.BRL, BigDecimal.ZERO)),
                false,
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 0)
        );

        AccountResponse response = AccountResponseMapper.toResponse(account);

        assertNotNull(response);
        assertFalse(response.isActive());
    }

    @Test
    void shouldReturnNullWhenAccountIsNull() {
        assertNull(AccountResponseMapper.toResponse(null));
    }

    private static Wallet wallet(Currency currency, BigDecimal balance) {
        return new Wallet(
                UUID.randomUUID(),
                currency,
                balance,
                Set.of(),
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 0)
        );
    }

    private static Person person() {
        DailyTransactionLimit limit = new DailyTransactionLimit(
                UUID.randomUUID(),
                Currency.BRL,
                new BigDecimal("10000.00"),
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 0)
        );

        return new Person(
                UUID.randomUUID(),
                "Agnes",
                "Galvao",
                new Document(PersonType.PF, "12345678901"),
                "agnes@example.com",
                "Valid@123",
                Set.of(limit),
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 0)
        );
    }
}

