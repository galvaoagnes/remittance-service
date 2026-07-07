package com.inter.remittance.domain.enums;

public enum Currency {
    BRL(false,true ),
    USD(true, false);

    private final boolean hasExchangeRate;
    private final boolean hasDailyTransactionLimit;

    Currency(boolean hasExchangeRate, boolean hasDailyTransactionLimit) {
        this.hasExchangeRate = hasExchangeRate;
        this.hasDailyTransactionLimit = hasDailyTransactionLimit;
    }

    public boolean hasExchangeRate() {
        return hasExchangeRate;
    }

    public boolean hasDailyTransactionLimit() {
        return hasDailyTransactionLimit;
    }

}
