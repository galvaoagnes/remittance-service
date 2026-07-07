package com.inter.remittance.application.service;

import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.domain.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindTransactionService {
    private final TransactionRepository transactionRepository;

    public FindTransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public PageResult<Transaction> findAllTransactions(
            UUID walletId,
            int page,
            int size
    ) {
        return transactionRepository.findTransactionsByWalletId(walletId, page, size);
    }
}
