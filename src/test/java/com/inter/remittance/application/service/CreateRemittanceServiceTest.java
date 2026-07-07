package com.inter.remittance.application.service;

import com.inter.remittance.application.command.CreateRemittanceCommand;
import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.entities.*;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;
import com.inter.remittance.domain.repositories.*;
import com.inter.remittance.domain.valueobjects.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRemittanceServiceTest {

    @Mock
    private RemittanceRepository remittanceRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Mock
    private GetCachedValuesService getCachedValuesService;

    @Mock
    private UpdateExchangeRateService updateExchangeRateService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private DailyTransactionLimitRepository dailyTransactionLimitRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private CreateRemittanceService service;

    @BeforeEach
    void setUp() {
        service = new CreateRemittanceService(
                remittanceRepository,
                accountRepository,
                currencyConversionService,
                getCachedValuesService,
                updateExchangeRateService,
                walletRepository,
                dailyTransactionLimitRepository,
                transactionRepository
        );
    }

    @Test
    void shouldProcessRemittanceSuccessfully() {
        Account sourceAccount = accountWithWalletAndLimit(Currency.BRL, new BigDecimal("1000.00"), new BigDecimal("10000.00"));
        Account destinationAccount = accountWithWalletAndLimit(Currency.USD, new BigDecimal("50.00"), new BigDecimal("10000.00"));
        CreateRemittanceCommand command = new CreateRemittanceCommand(
                sourceAccount.id(),
                destinationAccount.id(),
                Currency.BRL,
                Currency.USD,
                new BigDecimal("100.00")
        );

        when(accountRepository.findWithDetailsById(sourceAccount.id())).thenReturn(sourceAccount);
        when(accountRepository.findWithDetailsById(destinationAccount.id())).thenReturn(destinationAccount);
        when(getCachedValuesService.getCachedValue("exchangeRate", "#currency", BigDecimal.class))
                .thenReturn(new BigDecimal("5.00"));
        when(currencyConversionService.convert(any(BigDecimal.class), eq(Currency.BRL), eq(Currency.USD), eq(new BigDecimal("5.00"))))
                .thenReturn(new BigDecimal("500.00"));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(remittanceRepository.save(any(Remittance.class), any(UUID.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<Transaction, Remittance> result = service.process(command);

        assertEquals(1, result.size());
        Map.Entry<Transaction, Remittance> entry = result.entrySet().iterator().next();
        assertEquals(TransactionStatus.FINISHED_WITH_SUCCESS, entry.getKey().status());
        assertEquals(TransactionType.REMITTANCE, entry.getKey().type());
        assertEquals(0, entry.getValue().convertedCurrencyAmount().compareTo(new BigDecimal("500.00")));
        verify(walletRepository, times(2)).update(any(Wallet.class));
        verify(dailyTransactionLimitRepository).update(any(DailyTransactionLimit.class));
        verify(transactionRepository).save(any(Transaction.class));
        verify(remittanceRepository).save(any(Remittance.class), eq(sourceAccount.wallets().iterator().next().id()));
        verify(updateExchangeRateService, never()).execute(any());
    }

    @Test
    void shouldThrowWhenDailyLimitIsExceeded() {
        Account sourceAccount = accountWithWalletAndLimit(Currency.BRL, new BigDecimal("1000.00"), new BigDecimal("50.00"));
        Account destinationAccount = accountWithWalletAndLimit(Currency.USD, new BigDecimal("50.00"), new BigDecimal("10000.00"));
        CreateRemittanceCommand command = new CreateRemittanceCommand(
                sourceAccount.id(),
                destinationAccount.id(),
                Currency.BRL,
                Currency.USD,
                new BigDecimal("100.00")
        );

        when(accountRepository.findWithDetailsById(sourceAccount.id())).thenReturn(sourceAccount);
        when(accountRepository.findWithDetailsById(destinationAccount.id())).thenReturn(destinationAccount);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.process(command));

        assertEquals(ErrorCatalog.DAILY_LIMIT_EXCEEDED, ex.getMessage());
        verify(walletRepository, never()).update(any());
        verify(transactionRepository, never()).save(any());
        verify(remittanceRepository, never()).save(any(), any());
    }

    @Test
    void shouldThrowWhenSourceBalanceIsInsufficient() {
        Account sourceAccount = accountWithWalletAndLimit(Currency.BRL, new BigDecimal("10.00"), new BigDecimal("10000.00"));
        Account destinationAccount = accountWithWalletAndLimit(Currency.USD, new BigDecimal("50.00"), new BigDecimal("10000.00"));
        CreateRemittanceCommand command = new CreateRemittanceCommand(
                sourceAccount.id(),
                destinationAccount.id(),
                Currency.BRL,
                Currency.USD,
                new BigDecimal("100.00")
        );

        when(accountRepository.findWithDetailsById(sourceAccount.id())).thenReturn(sourceAccount);
        when(accountRepository.findWithDetailsById(destinationAccount.id())).thenReturn(destinationAccount);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.process(command));

        assertEquals(ErrorCatalog.INSUFFICIENT_FUNDS, ex.getMessage());
        verify(walletRepository, never()).update(any());
        verify(transactionRepository, never()).save(any());
        verify(remittanceRepository, never()).save(any(), any());
    }

    private Account accountWithWalletAndLimit(Currency currency, BigDecimal walletBalance, BigDecimal dailyLimitAmount) {
        DailyTransactionLimit limit = new DailyTransactionLimit(
                UUID.randomUUID(),
                Currency.BRL,
                dailyLimitAmount,
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
                walletBalance,
                new HashSet<>(),
                LocalDateTime.of(2026, 1, 1, 9, 0),
                LocalDateTime.of(2026, 1, 1, 9, 5)
        );

        return new Account(UUID.randomUUID(), person, Set.of(wallet), true, LocalDateTime.now(), LocalDateTime.now());
    }
}

