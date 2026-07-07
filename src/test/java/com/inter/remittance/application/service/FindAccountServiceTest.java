package com.inter.remittance.application.service;

import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.domain.repositories.AccountRepository;
import com.inter.remittance.domain.valueobjects.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private FindAccountService service;

    @BeforeEach
    void setUp() {
        service = new FindAccountService(accountRepository);
    }

    @Test
    void shouldFindAccountByIdDelegatingToRepository() {
        UUID accountId = UUID.randomUUID();
        Account expected = validAccount();

        when(accountRepository.findWithDetailsById(accountId)).thenReturn(expected);

        Account result = service.findAccountById(accountId);

        assertSame(expected, result);
        verify(accountRepository).findWithDetailsById(accountId);
    }

    @Test
    void shouldFindAllWithPaginationDelegatingToRepository() {
        PageResult<Account> expectedPage = new PageResult<>(Set.of(validAccount()), 0, 10, 1, 1);
        when(accountRepository.findAll(0, 10)).thenReturn(expectedPage);

        PageResult<Account> result = service.findAll(0, 10);

        assertEquals(expectedPage, result);
        verify(accountRepository).findAll(0, 10);
    }

    private Account validAccount() {
        Person person = new Person(
                UUID.randomUUID(),
                "Agnes",
                "Galvao",
                new Document(PersonType.PF, "12345678901"),
                "agnes@example.com",
                "Valid@123",
                null,
                null,
                null
        );

        Wallet wallet = new Wallet(
                UUID.randomUUID(),
                Currency.BRL,
                BigDecimal.TEN,
                new HashSet<>(),
                null,
                null
        );

        return new Account(UUID.randomUUID(), person, Set.of(wallet), true, null, null);
    }
}

