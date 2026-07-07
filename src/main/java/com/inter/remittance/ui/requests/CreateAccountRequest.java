package com.inter.remittance.ui.requests;

import com.inter.remittance.domain.enums.PersonType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record CreateAccountRequest(
        @NotBlank
        String name,

        String lastName,

        @NotBlank
        PersonType personType,

        @NotBlank
        String documentNumber,

        @NotBlank
        @Size(min = 8, max = 64)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$"
        )
        String password,

        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )
        @NotBlank
        String email
) {}
