package com.inter.remittance.domain.entities;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Remittance(
        UUID id,
        Transaction transaction,
        BigDecimal convertedCurrencyAmount,
        BigDecimal exchangeRate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {



    public Remittance {
        id = (id == null) ? UUID.randomUUID() : id;
        validateConvertedCurrencyAmount(convertedCurrencyAmount);
        validateExchangeRate(exchangeRate);
        validateCreatedAt(createdAt);
        validateUpdatedAt(updatedAt);
    }

    public static Remittance createNew(
            Transaction transaction,
            BigDecimal exchangeRate,
            BigDecimal convertedAmount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        validateConvertedCurrencyAmount(convertedAmount);
        validateExchangeRate(exchangeRate);
        validateConvertedAmount(convertedAmount);

        return new Remittance(
                UUID.randomUUID(),
                transaction,
                convertedAmount,
                exchangeRate,
                createdAt,
                updatedAt
        );
    }

    private static void validateConvertedCurrencyAmount(BigDecimal originalCurrencyAmount) {
        if (originalCurrencyAmount == null || originalCurrencyAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCatalog.INVALID_ORIGINAL_CURRENCY_AMOUNT);
        }
    }

    private static void validateExchangeRate(BigDecimal exchangeRate) {
        if (exchangeRate == null || exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCatalog.INVALID_EXCHANGE_RATE);
        }
    }

    private static void validateConvertedAmount(BigDecimal convertedAmount) {
        if (convertedAmount == null || convertedAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCatalog.INVALID_CONVERTED_AMOUNT);
        }
    }

    private static void validateCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new BusinessException(ErrorCatalog.INVALID_CREATED_AT);
        }
    }

    private static void validateUpdatedAt(LocalDateTime updatedAt) {
        if (updatedAt == null) {
            throw new BusinessException(ErrorCatalog.INVALID_UPDATED_AT);
        }
    }
}