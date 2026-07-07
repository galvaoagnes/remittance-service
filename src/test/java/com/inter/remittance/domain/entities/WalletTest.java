package com.inter.remittance.domain.entities;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.enums.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletTest {

    @Test
    void shouldGenerateIdAndDefaultTransactionsWhenNull() {
        Wallet wallet = new Wallet(
                null,
                Currency.BRL,
                BigDecimal.ZERO,
                null,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 5)
        );

        assertNotNull(wallet.id());
        assertNotNull(wallet.transactions());
        assertEquals(0, wallet.transactions().size());
    }

    @Test
    void shouldThrowWhenCurrencyIsNull() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> new Wallet(
                        UUID.randomUUID(),
                        null,
                        BigDecimal.ZERO,
                        new HashSet<>(),
                        LocalDateTime.of(2026, 1, 1, 10, 0),
                        LocalDateTime.of(2026, 1, 1, 10, 5)
                )
        );

        assertEquals(ErrorCatalog.INVALID_CURRENCY, ex.getMessage());
    }

    @Test
    void shouldThrowWhenBalanceIsNegative() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> new Wallet(
                        UUID.randomUUID(),
                        Currency.BRL,
                        new BigDecimal("-0.01"),
                        new HashSet<>(),
                        LocalDateTime.of(2026, 1, 1, 10, 0),
                        LocalDateTime.of(2026, 1, 1, 10, 5)
                )
        );

        assertEquals(ErrorCatalog.INVALID_AMOUNT, ex.getMessage());
    }

    @Test
    void shouldThrowWhenValidateAvailableBalanceHasInvalidAmount() {
        Wallet wallet = validWallet(new BigDecimal("100.00"));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> wallet.validateAvailableBalance(BigDecimal.ZERO));

        assertEquals(ErrorCatalog.INVALID_AMOUNT, ex.getMessage());
    }

    @Test
    void shouldThrowWhenValidateAvailableBalanceHasInsufficientFunds() {
        Wallet wallet = validWallet(new BigDecimal("100.00"));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> wallet.validateAvailableBalance(new BigDecimal("100.01")));

        assertEquals(ErrorCatalog.INSUFFICIENT_FUNDS, ex.getMessage());
    }

    @Test
    void shouldWithdrawAndReturnUpdatedWallet() {
        Wallet wallet = validWallet(new BigDecimal("100.00"));

        Wallet updated = wallet.withdraw(new BigDecimal("40.00"));

        assertEquals(0, updated.balance().compareTo(new BigDecimal("60.00")));
        assertEquals(wallet.id(), updated.id());
        assertNotNull(updated.updatedAt());
    }

    @Test
    void shouldAddBalanceAndReturnUpdatedWallet() {
        Wallet wallet = validWallet(new BigDecimal("100.00"));

        Wallet updated = wallet.addBalance(new BigDecimal("25.00"));

        assertEquals(0, updated.balance().compareTo(new BigDecimal("125.00")));
    }

    @Test
    void shouldThrowWhenAddBalanceAmountIsInvalid() {
        Wallet wallet = validWallet(new BigDecimal("100.00"));

        BusinessException ex = assertThrows(BusinessException.class, () -> wallet.addBalance(BigDecimal.ZERO));

        assertEquals(ErrorCatalog.INVALID_AMOUNT, ex.getMessage());
    }

    @Test
    void shouldCreateWalletWithZeroBalance() {
        Wallet wallet = Wallet.create(Currency.USD);

        assertEquals(Currency.USD, wallet.currency());
        assertEquals(0, wallet.balance().compareTo(BigDecimal.ZERO));
        assertNotNull(wallet.id());
    }

    @Test
    void shouldCreateDefaultWalletsForAllCurrencies() {
        Set<Wallet> wallets = Wallet.createDefaultWallets();

        assertEquals(Currency.values().length, wallets.size());
        assertEquals(Set.of(Currency.BRL, Currency.USD), wallets.stream().map(Wallet::currency).collect(java.util.stream.Collectors.toSet()));
        assertThrows(UnsupportedOperationException.class, () -> wallets.add(validWallet(BigDecimal.ZERO)));
    }

    @Test
    void shouldExposeDefaultOriginCurrencyAsBrl() {
        assertEquals(Currency.BRL, Wallet.DEFAULT_ORIGIN_CURRENCY);
    }

    private Wallet validWallet(BigDecimal balance) {
        return new Wallet(
                UUID.randomUUID(),
                Currency.BRL,
                balance,
                new HashSet<>(),
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 5)
        );
    }
}

