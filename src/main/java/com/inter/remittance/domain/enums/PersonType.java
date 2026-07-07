package com.inter.remittance.domain.enums;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.commom.Utils;

import java.math.BigDecimal;
import java.util.Map;


public enum PersonType {

    PF() {
        @Override
        public void validateDocument(String documentNumber) {
            if (!Utils.isValidCpfFormat(documentNumber)) {
                throw new BusinessException(ErrorCatalog.INVALID_CPF_NUMBER);
            }
        }
    },

    PJ() {
        @Override
        public void validateDocument(String documentNumber) {
            if (!Utils.isValidCnpjFormat(documentNumber)) {
                throw new BusinessException(ErrorCatalog.INVALID_CNPJ_NUMBER);
            }
        }
    };

    public abstract void validateDocument(String documentNumber);

    public Map<Currency, BigDecimal> getInitialDailyTransactionLimit() {
        return switch (this) {
            case PF -> Map.ofEntries(
                    Map.entry(Currency.BRL, new BigDecimal(10_000))
        );
            case PJ -> Map.ofEntries(
                    Map.entry(Currency.BRL, new BigDecimal(30_000))
            );
        };
    }
}
