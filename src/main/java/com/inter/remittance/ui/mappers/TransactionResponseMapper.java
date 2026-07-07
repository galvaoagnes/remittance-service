package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.ui.responses.TransactionResponse;

public final class TransactionResponseMapper {
    private TransactionResponseMapper() {
    }

    public static TransactionResponse toResponse(Transaction transaction) {
        if (transaction == null) return null;

        return new TransactionResponse(
                transaction.id(),
                transaction.sourceWallet() == null ? null : transaction.sourceWallet().id(),
                transaction.destinationWallet() == null ? null : transaction.destinationWallet().id(),
                transaction.amount(),
                transaction.status(),
                transaction.type()
        );
    }
}
