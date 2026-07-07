package com.inter.remittance.application.command;

import com.inter.remittance.domain.enums.PersonType;

public record CreateAccountCommand(
        String name,
        String lastName,
        PersonType personType,
        String documentNumber,
        String password,
        String email
) { }