package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.ui.responses.WalletCreatedResponse;

public final class WalletResponseMapper {

    public static WalletCreatedResponse toResponse(Wallet wallet) {
        if (wallet == null) {
            return null;
        }

        return new WalletCreatedResponse(
                wallet.id(),
                wallet.currency(),
                wallet.balance()
        );
    }
}
