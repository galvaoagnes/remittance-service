package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.ui.responses.AccountResponse;

import java.util.Collections;
import java.util.stream.Collectors;

public final class AccountResponseMapper {

    private AccountResponseMapper() {
    }

    public static AccountResponse toResponse(Account account) {
        if (account == null) {
            return null;
        }

        return new AccountResponse(
                account.id(),
                PersonResponseMapper.toResponse(
                        account.person()
                ),
                account.wallets() == null
                        ? Collections.emptySet()
                        : account.wallets().stream()
                          .map(WalletResponseMapper::toResponse)
                          .collect(Collectors.toSet()),
                account.isActive(),
                account.createdAt(),
                account.updatedAt()
        );
    }
}