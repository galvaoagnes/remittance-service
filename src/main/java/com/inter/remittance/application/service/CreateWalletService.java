package com.inter.remittance.application.service;

import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
public class CreateWalletService {
    private final AccountRepository accountRepository;

    public CreateWalletService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    private static final Logger log = LoggerFactory.getLogger(CreateWalletService.class);

    public Wallet create(
            UUID accountId,
            Currency currency
    ){
        log.info("Creating new wallet for account with id {} and currency {}",
                accountId,
                currency
        );

        Account account = accountRepository.findWithDetailsById(accountId);

        Wallet wallet = new Wallet(
                UUID.randomUUID(),
                currency,
                BigDecimal.ZERO,
                Collections.emptySet(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        accountRepository.save(account.addWallet(wallet));

        log.info("Wallet created successfully for account with id {} and currency {}",
                accountId,
                currency
        );

        return wallet;
    }
}
