package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.Remittance;
import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.ui.responses.RemittanceResponse;

import java.util.Map;

public final class RemittanceResponseMapper {




    public static RemittanceResponse toResponse(Map<Transaction, Remittance> createResult) {
        Transaction transaction = createResult.keySet()
                .iterator()
                .next();
        Remittance remittance = createResult.get(transaction);

        if (transaction == null || remittance == null) return null;


        return new RemittanceResponse(
                remittance.id(),
                transaction.id(),
                transaction.amount(),
                remittance.convertedCurrencyAmount(),
                remittance.exchangeRate(),
                transaction.status(),
                remittance.createdAt()
        );
    }
}