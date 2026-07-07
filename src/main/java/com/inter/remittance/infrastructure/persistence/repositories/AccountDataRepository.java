package com.inter.remittance.infrastructure.persistence.repositories;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.repositories.AccountRepository;
import com.inter.remittance.infrastructure.mappers.AccountMapper;
import com.inter.remittance.infrastructure.persistence.entities.AccountEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class AccountDataRepository implements AccountRepository {

    private final AccountJpaRepository repository;

    public AccountDataRepository(AccountJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account save(Account account) {
        AccountEntity entity = AccountMapper.toEntity(account);

        entity = repository.save(entity);

        return AccountMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public void update(UUID id, boolean isActive) {
        AccountEntity entity = repository.findById(id).orElseThrow(
                () -> new BusinessException(ErrorCatalog.ACCOUNT_NOT_FOUND)
        );

        entity.seIsActive(isActive);
    }

    @Override
    public Account findWithDetailsById(UUID accountId) {
        AccountEntity entity = repository.findWithDetailsById(accountId);

        if (entity == null) {
            throw new BusinessException(ErrorCatalog.ACCOUNT_NOT_FOUND);
        }

        return AccountMapper.toDomain(entity);
    }

    @Override
    public PageResult<Account> findAll(
            int page,
            int size
    ) {
        Page<AccountEntity> result =
                repository.findAll(PageRequest.of(page, size));

        return new PageResult<>(
                result.getContent()
                        .stream()
                        .map(AccountMapper::toDomain)
                        .collect(Collectors.toSet()),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
}
