package com.inter.remittance.domain.valueobjects;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.enums.PersonType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DocumentTest {

    @Test
    void shouldCreateDocumentWhenPfNumberIsValid() {
        Document document = new Document(PersonType.PF, "12345678901");

        assertEquals(PersonType.PF, document.type());
        assertEquals("12345678901", document.value());
    }

    @Test
    void shouldThrowWhenPfNumberIsInvalid() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> new Document(PersonType.PF, "123"));

        assertEquals(ErrorCatalog.INVALID_CPF_NUMBER, ex.getMessage());
    }

    @Test
    void shouldThrowWhenPjNumberIsInvalid() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> new Document(PersonType.PJ, "123"));

        assertEquals(ErrorCatalog.INVALID_CNPJ_NUMBER, ex.getMessage());
    }
}

