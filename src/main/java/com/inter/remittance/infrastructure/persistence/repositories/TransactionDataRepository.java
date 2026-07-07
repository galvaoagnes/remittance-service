package com.inter.remittance.infrastructure.persistence.repositories;

import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.entities.Transaction;
import com.inter.remittance.domain.repositories.TransactionRepository;
import com.inter.remittance.infrastructure.mappers.TransactionMapper;
import com.inter.remittance.infrastructure.persistence.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TransactionDataRepository implements TransactionRepository {

    private final TransactionJpaRepository repository;

    public TransactionDataRepository(TransactionJpaRepository repository) {
        this.repository = repository;
    }


    @Override
    public PageResult<Transaction> findTransactionsByWalletId(UUID walletId, int page, int size) {
        Page<TransactionEntity> result =
                repository.findByWalletId(
                        walletId,
                        PageRequest.of(page, size)
                );

        return new PageResult<>(
                result.getContent()
                        .stream()
                        .map(TransactionMapper::toDomain)
                        .collect(Collectors.toSet()),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public Transaction save(Transaction transaction) {
         TransactionEntity entity = repository.save(TransactionMapper.toEntity(transaction));

         return TransactionMapper.toDomain(entity);
    }
}
