package com.inter.remittance.application.service;

import com.inter.remittance.domain.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private UpdateAccountService service;

    @BeforeEach
    void setUp() {
        service = new UpdateAccountService(accountRepository);
    }

    @Test
    void shouldUpdateAccountStatusDelegatingToRepository() {
        UUID id = UUID.randomUUID();

        service.updateActiveStatuses(id, false);

        verify(accountRepository).update(id, false);
    }
}

