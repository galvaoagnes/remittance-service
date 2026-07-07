package com.inter.remittance.application.service;

import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.domain.repositories.AccountRepository;
import com.inter.remittance.domain.valueobjects.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateWalletServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private CreateWalletService service;

    @BeforeEach
    void setUp() {
        service = new CreateWalletService(accountRepository);
    }

    @Test
    void shouldCreateWalletAndPersistUpdatedAccount() {
        Account account = accountWithCurrencies(Set.of(Currency.BRL));
        when(accountRepository.findWithDetailsById(account.id())).thenReturn(account);

        Wallet created = service.create(account.id(), Currency.USD);

        assertNotNull(created.id());
        assertEquals(Currency.USD, created.currency());
        assertEquals(0, created.balance().compareTo(BigDecimal.ZERO));

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());
        assertTrue(accountCaptor.getValue().wallets().stream().anyMatch(w -> w.currency() == Currency.USD));
    }

    private Account accountWithCurrencies(Set<Currency> currencies) {
        Person person = new Person(
                UUID.randomUUID(),
                "Agnes",
                "Galvao",
                new Document(PersonType.PF, "12345678901"),
                "agnes@example.com",
                "Valid@123",
                null,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 5)
        );

        Set<Wallet> wallets = new HashSet<>();
        for (Currency currency : currencies) {
            wallets.add(new Wallet(
                    UUID.randomUUID(),
                    currency,
                    BigDecimal.ZERO,
                    new HashSet<>(),
                    LocalDateTime.of(2026, 1, 1, 10, 0),
                    LocalDateTime.of(2026, 1, 1, 10, 5)
            ));
        }

        return new Account(UUID.randomUUID(), person, wallets, true, LocalDateTime.now(), LocalDateTime.now());
    }
}

