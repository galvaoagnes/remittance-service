package com.inter.remittance.application.service;

import com.inter.remittance.application.command.CreateDepositCommand;
import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.DailyTransactionLimit;
import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;
import com.inter.remittance.domain.repositories.AccountRepository;
import com.inter.remittance.domain.repositories.TransactionRepository;
import com.inter.remittance.domain.repositories.WalletRepository;
import com.inter.remittance.domain.valueobjects.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateDepositServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    private CreateDepositService service;

    @BeforeEach
    void setUp() {
        service = new CreateDepositService(walletRepository, transactionRepository, accountRepository);
    }

    @Test
    void shouldProcessDepositSuccessfully() {
        Account account = accountWithWallet(Currency.BRL, new BigDecimal("100.00"));
        CreateDepositCommand command = new CreateDepositCommand(account.id(), Currency.BRL, new BigDecimal("25.00"));

        when(accountRepository.findWithDetailsById(account.id())).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = service.process(command);

        assertEquals(TransactionStatus.FINISHED_WITH_SUCCESS, result.status());
        assertEquals(TransactionType.DEPOSIT, result.type());
        assertEquals(0, result.amount().compareTo(new BigDecimal("25.00")));
        verify(walletRepository).update(any(Wallet.class));
        verify(transactionRepository, times(2)).save(any(Transaction.class));
        verify(transactionRepository, never()).save(null);
    }

    @Test
    void shouldMarkDepositAsFailureWhenBalanceUpdateFails() {
        Account account = accountWithWallet(Currency.BRL, new BigDecimal("100.00"));
        CreateDepositCommand command = new CreateDepositCommand(account.id(), Currency.BRL, new BigDecimal("25.00"));

        when(accountRepository.findWithDetailsById(account.id())).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        org.mockito.Mockito.doThrow(new RuntimeException("wallet update failed")).when(walletRepository).update(any(Wallet.class));

        Transaction result = service.process(command);

        assertEquals(TransactionStatus.FINISHED_WITH_FAILURE, result.status());
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    void shouldThrowWhenWalletDoesNotExistForDeposit() {
        Account account = accountWithWallet(Currency.USD, new BigDecimal("100.00"));
        CreateDepositCommand command = new CreateDepositCommand(account.id(), Currency.BRL, new BigDecimal("25.00"));

        when(accountRepository.findWithDetailsById(account.id())).thenReturn(account);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.process(command));

        assertEquals(ErrorCatalog.WALLET_NOT_FOUND, ex.getMessage());
        verify(transactionRepository, never()).save(any());
        verify(walletRepository, never()).update(any());
    }

    private Account accountWithWallet(Currency currency, BigDecimal balance) {
        DailyTransactionLimit limit = new DailyTransactionLimit(
                UUID.randomUUID(),
                Currency.BRL,
                new BigDecimal("10000.00"),
                LocalDateTime.of(2026, 1, 1, 9, 0),
                LocalDateTime.of(2026, 1, 1, 9, 5)
        );
        Set<DailyTransactionLimit> limits = new HashSet<>();
        limits.add(limit);

        Person person = new Person(
                UUID.randomUUID(),
                "Agnes",
                "Galvao",
                new Document(PersonType.PF, "12345678901"),
                "agnes@example.com",
                "Valid@123",
                limits,
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 8, 5)
        );

        Wallet wallet = new Wallet(
                UUID.randomUUID(),
                currency,
                balance,
                new HashSet<>(),
                LocalDateTime.of(2026, 1, 1, 9, 0),
                LocalDateTime.of(2026, 1, 1, 9, 5)
        );

        return new Account(UUID.randomUUID(), person, Set.of(wallet), true, LocalDateTime.now(), LocalDateTime.now());
    }
}

