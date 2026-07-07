package com.inter.remittance.application.security;

import com.inter.remittance.security.Role;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTest {

    @Test
    void shouldExposeAllRoles() {
        assertEquals(List.of(Role.USER, Role.ADMIN), List.of(Role.values()));
    }
}

