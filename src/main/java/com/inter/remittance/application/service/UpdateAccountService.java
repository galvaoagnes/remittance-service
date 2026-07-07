package com.inter.remittance.application.service;

import com.inter.remittance.domain.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateAccountService {
    private final AccountRepository accountRepository;


    public UpdateAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(UpdateAccountService.class);
   public void updateActiveStatuses(
            UUID id,
            boolean isActive
    ){
        log.info("Updating account active status for account with id {} to {}",
                id,
                isActive
        );
        accountRepository.update(id,isActive);
    }
}
