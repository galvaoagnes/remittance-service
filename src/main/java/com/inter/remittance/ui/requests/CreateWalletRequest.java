package com.inter.remittance.ui.requests;


import com.inter.remittance.domain.enums.Currency;
import jakarta.validation.constraints.NotNull;

public record CreateWalletRequest(
        @NotNull
        Currency currency
){}
