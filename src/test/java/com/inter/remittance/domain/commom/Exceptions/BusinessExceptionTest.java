package com.inter.remittance.domain.commom.Exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessExceptionTest {

    @Test
    void shouldKeepExceptionMessage() {
        BusinessException ex = new BusinessException("any message");

        assertEquals("any message", ex.getMessage());
    }
}

