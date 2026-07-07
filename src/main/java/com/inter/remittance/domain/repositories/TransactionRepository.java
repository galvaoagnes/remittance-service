package com.inter.remittance.domain.repositories;

import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.entities.Transaction;

import java.util.UUID;

public interface TransactionRepository {
    PageResult<Transaction> findTransactionsByWalletId(UUID walletId, int page, int size);

    Transaction save(Transaction transaction);
}
