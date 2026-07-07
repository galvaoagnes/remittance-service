package com.inter.remittance.ui.requests;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String document,

        @NotBlank
        String  password
) {
}
