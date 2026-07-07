package com.inter.remittance.domain.repositories;

import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.PageResult;

import java.util.UUID;

public interface AccountRepository {
    Account save(Account account);
    void update(UUID id, boolean isActive);
    Account findWithDetailsById(UUID accountId);
    PageResult<Account> findAll(int page, int size);
}
