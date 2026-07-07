package com.inter.remittance.application.security;

import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.domain.repositories.PersonRepository;
import com.inter.remittance.domain.valueobjects.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoadUserDetailsTest {

    @Mock
    private PersonRepository personRepository;

    private LoadUserDetails service;

    @BeforeEach
    void setUp() {
        service = new LoadUserDetails(personRepository);
    }

    @Test
    void shouldLoadUserWhenPersonExists() {
        when(personRepository.findByDocumentValue("12345678901")).thenReturn(validPerson());

        UserDetails userDetails = service.loadUserByUsername("12345678901");

        assertEquals("12345678901", userDetails.getUsername());
        assertEquals("Valid@123", userDetails.getPassword());
        assertEquals("ROLE_PF", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void shouldThrowWhenPersonDoesNotExist() {
        when(personRepository.findByDocumentValue("12345678901")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.loadUserByUsername("12345678901"));

        assertEquals("User not found", ex.getMessage());
    }

    private Person validPerson() {
        return new Person(
                UUID.randomUUID(),
                "Agnes",
                "Galvao",
                new Document(PersonType.PF, "12345678901"),
                "agnes@example.com",
                "Valid@123",
                null,
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 5)
        );
    }
}

