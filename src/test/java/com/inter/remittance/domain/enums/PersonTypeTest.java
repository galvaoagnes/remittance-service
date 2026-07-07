package com.inter.remittance.domain.enums;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonTypeTest {

    @Test
    void shouldValidatePfDocumentWhenFormatIsCorrect() {
        PersonType.PF.validateDocument("12345678901");
    }

    @Test
    void shouldThrowWhenPfDocumentFormatIsInvalid() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> PersonType.PF.validateDocument("123"));

        assertEquals(ErrorCatalog.INVALID_CPF_NUMBER, ex.getMessage());
    }

    @Test
    void shouldValidatePjDocumentWhenFormatIsCorrect() {
        PersonType.PJ.validateDocument("12345678000199");
    }

    @Test
    void shouldThrowWhenPjDocumentFormatIsInvalid() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> PersonType.PJ.validateDocument("123"));

        assertEquals(ErrorCatalog.INVALID_CNPJ_NUMBER, ex.getMessage());
    }

    @Test
    void shouldReturnInitialLimitForPf() {
        Map<Currency, BigDecimal> limits = PersonType.PF.getInitialDailyTransactionLimit();

        assertEquals(1, limits.size());
        assertEquals(0, limits.get(Currency.BRL).compareTo(new BigDecimal("10000")));
    }

    @Test
    void shouldReturnInitialLimitForPj() {
        Map<Currency, BigDecimal> limits = PersonType.PJ.getInitialDailyTransactionLimit();

        assertEquals(1, limits.size());
        assertEquals(0, limits.get(Currency.BRL).compareTo(new BigDecimal("30000")));
    }
}

