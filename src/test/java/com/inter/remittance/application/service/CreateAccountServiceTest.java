package com.inter.remittance.application.service;

import com.inter.remittance.application.command.CreateAccountCommand;
import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.domain.repositories.AccountRepository;
import com.inter.remittance.domain.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CreateAccountService service;

    @BeforeEach
    void setUp() {
        service = new CreateAccountService(accountRepository, personRepository, passwordEncoder);
    }

    @Test
    void shouldThrowWhenDocumentAlreadyExists() {
        CreateAccountCommand command = validCommand();
        when(personRepository.existsByDocumentValue(command.documentNumber())).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.execute(command));

        assertEquals(ErrorCatalog.DOCUMENT_ALREADY_EXISTS, ex.getMessage());
        verify(accountRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void shouldCreateAccountWithDefaultWalletsAndEncodedPassword() {
        CreateAccountCommand command = validCommand();
        when(personRepository.existsByDocumentValue(command.documentNumber())).thenReturn(false);
        when(passwordEncoder.encode(command.password())).thenReturn("Encoded@123");
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account result = service.execute(command);

        assertNotNull(result.id());
        assertTrue(result.isActive());
        assertEquals(command.name(), result.person().name());
        assertEquals(command.lastName(), result.person().lastName());
        assertEquals(command.documentNumber(), result.person().document().value());
        assertEquals(command.email(), result.person().email());
        assertEquals("Encoded@123", result.person().password());

        Set<Currency> currenciesInWallet = result.wallets().stream()
                .map(Wallet::currency)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(Arrays.stream(Currency.values()).collect(java.util.stream.Collectors.toSet()), currenciesInWallet);
        assertTrue(result.wallets().stream().allMatch(wallet -> wallet.balance().compareTo(BigDecimal.ZERO) == 0));

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());
        Person persistedPerson = accountCaptor.getValue().person();
        assertEquals("Encoded@123", persistedPerson.password());
        verify(passwordEncoder).encode(command.password());
    }

    private CreateAccountCommand validCommand() {
        return new CreateAccountCommand(
                "Agnes",
                "Galvao",
                PersonType.PF,
                "12345678901",
                "Valid@123",
                "agnes@example.com"
        );
    }
}

