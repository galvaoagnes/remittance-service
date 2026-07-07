package com.inter.remittance.domain.commom;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {

    @Test
    void shouldValidateEmailFormat() {
        assertTrue(Utils.isValidEmailFormat("agnes@example.com"));
        assertFalse(Utils.isValidEmailFormat("invalid-email"));
    }

    @Test
    void shouldValidateCpfFormat() {
        assertTrue(Utils.isValidCpfFormat("12345678901"));
        assertFalse(Utils.isValidCpfFormat("123"));
    }

    @Test
    void shouldValidateCnpjFormat() {
        assertTrue(Utils.isValidCnpjFormat("12345678000199"));
        assertFalse(Utils.isValidCnpjFormat("123"));
    }

    @Test
    void shouldValidatePasswordFormat() {
        assertTrue(Utils.isValidPasswordFormat("Valid@123"));
        assertFalse(Utils.isValidPasswordFormat("weak"));
    }
}

