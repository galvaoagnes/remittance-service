package com.inter.remittance.domain.entities;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record Wallet(
        UUID id,
        Currency currency,
        BigDecimal balance,
        Set<Transaction> transactions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public Wallet {
        id = (id == null) ? UUID.randomUUID() : id;
        validateCurrency(currency);
        validateBalance(balance);
        transactions = (transactions == null) ? new HashSet<>() : transactions;
    }

    public void validateAvailableBalance(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCatalog.INVALID_AMOUNT);
        }

        if (balance.compareTo(amount) < 0) {
            throw new BusinessException(ErrorCatalog.INSUFFICIENT_FUNDS);
        }
    }

    public Wallet withdraw(BigDecimal amount) {
        validateAvailableBalance(amount);
        return new Wallet(
                this.id,
                this.currency,
                this.balance.subtract(amount),
                this.transactions,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    public Wallet addBalance(BigDecimal amount) {
        validateAmountToChange(amount);
        return new Wallet(
                this.id,
                this.currency,
                this.balance.add(amount),
                this.transactions,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    private static void validateAmountToChange(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCatalog.INVALID_AMOUNT);
        }
    }

    public static final Currency DEFAULT_ORIGIN_CURRENCY = Currency.BRL;

    public static Wallet create(Currency currency) {
        return new Wallet(
                UUID.randomUUID(),
                currency,
                BigDecimal.ZERO,
                new HashSet<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static Set<Wallet> createDefaultWallets() {
        return Arrays.stream(Currency.values())
                .map(Wallet::create)
                .collect(Collectors.toUnmodifiableSet());
    }

    private static void validateCurrency(Currency currency) {
        if (currency == null) {
            throw new BusinessException(ErrorCatalog.INVALID_CURRENCY);
        }
    }

    private static void validateBalance(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCatalog.INVALID_AMOUNT);
        }
    }
}