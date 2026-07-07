package com.inter.remittance.application.service;

import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindAccountService {
    private final AccountRepository accountRepository;

    public FindAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

   public Account findAccountById(UUID id){

       return accountRepository.findWithDetailsById(id);
   }

   public PageResult<Account> findAll(
           int page,
           int size
   ){

       return accountRepository.findAll(
               page,
               size
       );
   }

}
