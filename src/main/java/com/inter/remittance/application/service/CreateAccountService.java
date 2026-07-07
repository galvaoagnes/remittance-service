package com.inter.remittance.application.service;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.repositories.AccountRepository;
import com.inter.remittance.application.command.CreateAccountCommand;
import com.inter.remittance.domain.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.inter.remittance.domain.entities.Person.createNewPerson;

@Service
public class CreateAccountService {

    private final AccountRepository accountRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateAccountService(AccountRepository accountRepository, PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final Logger log = LoggerFactory.getLogger(CreateAccountService.class);

    public Account execute(CreateAccountCommand command){
        if (personRepository.existsByDocumentValue(command.documentNumber())) {
            throw new BusinessException(ErrorCatalog.DOCUMENT_ALREADY_EXISTS);
        }

        Person person = createNewPerson(
               command.name(),
               command.lastName(),
               command.documentNumber(),
               command.personType(),
               passwordEncoder.encode(command.password()),
               command.email(),
               LocalDateTime.now(),
               LocalDateTime.now()
       );

        Account persistedAccount = accountRepository.save(
                new Account(
                        UUID.randomUUID(),
                        person,
                        Wallet.createDefaultWallets(),
                        true,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );

        log.info("Account id {} created successfully ",
                persistedAccount.id()
        );

       return persistedAccount;
   }
}
