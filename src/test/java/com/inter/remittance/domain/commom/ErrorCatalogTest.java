package com.inter.remittance.domain.commom;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorCatalogTest {

    @Test
    void shouldExposeKnownErrorMessages() {
        assertEquals("Person not found", ErrorCatalog.PERSON_NOT_FOUND);
        assertEquals("Invalid email", ErrorCatalog.INVALID_EMAIL);
        assertEquals("Insufficient funds", ErrorCatalog.INSUFFICIENT_FUNDS);
    }
}

