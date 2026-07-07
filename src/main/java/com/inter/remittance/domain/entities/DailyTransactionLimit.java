package com.inter.remittance.domain.entities;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.PersonType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record DailyTransactionLimit(
        UUID id,
        Currency currency,
        BigDecimal amount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public DailyTransactionLimit debit(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCatalog.INVALID_AMOUNT);
        }

        validateAvailableDailyLimit(value);

        return new DailyTransactionLimit(
                this.id,
                this.currency,
                this.amount.subtract(value),
                this.createdAt,
                LocalDateTime.now()
        );
    }

    public static Set<DailyTransactionLimit> setInitialDailyTransactionLimit(
            Set<DailyTransactionLimit> actualDailyTransactionLimits,
            PersonType personType
    ) {
        DailyTransactionLimit helper = new DailyTransactionLimit(
                UUID.randomUUID(),
                Currency.BRL,
                BigDecimal.ZERO,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        if (actualDailyTransactionLimits == null || actualDailyTransactionLimits.isEmpty()) {
            return helper.createNewDailyLimits(personType);
        }

        return helper.updateDailyLimits(actualDailyTransactionLimits, personType);
    }

    public void validateAvailableDailyLimit(BigDecimal amount) {
        if (amount.compareTo(this.amount()) > 0) {
            throw new BusinessException(ErrorCatalog.DAILY_LIMIT_EXCEEDED);
        }
    }

    private Set<DailyTransactionLimit> updateDailyLimits(
            Set<DailyTransactionLimit> actualDailyTransactionLimits,
            PersonType personType
    ){
        return actualDailyTransactionLimits.stream()
                .map(limit -> new DailyTransactionLimit(
                        limit.id(),
                        limit.currency(),
                        personType.getInitialDailyTransactionLimit().get(limit.currency()),
                        limit.createdAt,
                        LocalDateTime.now()
                ))
                .collect(java.util.stream.Collectors.toSet());
    }

    private Set<DailyTransactionLimit> createNewDailyLimits(PersonType personType){
        Set<DailyTransactionLimit> limits = new HashSet<>();

        for (Currency currency : Currency.values()) {
            if (currency.hasDailyTransactionLimit()) {
                limits.add(
                        new DailyTransactionLimit(
                                UUID.randomUUID(),
                                currency,
                                personType.getInitialDailyTransactionLimit().get(currency()),
                                LocalDateTime.now(),
                                LocalDateTime.now()
                        )
                );
            }
        }

        return limits;
    }
}