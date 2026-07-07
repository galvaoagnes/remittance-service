package com.inter.remittance.infrastructure.persistence.repositories;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.entities.Account;
import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.entities.Wallet;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.domain.valueobjects.Document;
import com.inter.remittance.infrastructure.mappers.AccountMapper;
import com.inter.remittance.infrastructure.persistence.entities.AccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountDataRepositoryTest {

    @Mock
    private AccountJpaRepository jpaRepository;

    private AccountDataRepository repository;

    @BeforeEach
    void setUp() {
        repository = new AccountDataRepository(jpaRepository);
    }

    @Test
    void shouldSaveAccountMappingToAndFromEntity() {
        Account account = validAccount();
        when(jpaRepository.save(any(AccountEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account result = repository.save(account);

        assertEquals(account.id(), result.id());
        assertEquals(account.person().document().value(), result.person().document().value());
        assertEquals(account.wallets().size(), result.wallets().size());
        verify(jpaRepository).save(any(AccountEntity.class));
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingAccount() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(java.util.Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> repository.update(id, true));

        assertEquals(ErrorCatalog.ACCOUNT_NOT_FOUND, ex.getMessage());
    }

    @Test
    void shouldUpdateActiveStatusWhenAccountExists() {
        UUID id = UUID.randomUUID();
        AccountEntity entity = new AccountEntity();
        entity.setId(id);
        entity.seIsActive(true);
        when(jpaRepository.findById(id)).thenReturn(java.util.Optional.of(entity));

        repository.update(id, false);

        assertFalse(entity.isActive());
        verify(jpaRepository).findById(id);
    }

    @Test
    void shouldThrowWhenFindWithDetailsReturnsNull() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findWithDetailsById(id)).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> repository.findWithDetailsById(id));

        assertEquals(ErrorCatalog.ACCOUNT_NOT_FOUND, ex.getMessage());
    }

    @Test
    void shouldFindWithDetailsMappingToDomain() {
        AccountEntity entity = AccountMapper.toEntity(validAccount());
        when(jpaRepository.findWithDetailsById(entity.getId())).thenReturn(entity);

        Account result = repository.findWithDetailsById(entity.getId());

        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getPerson().getDocument().getValue(), result.person().document().value());
        assertEquals(entity.getWallets().size(), result.wallets().size());
    }

    @Test
    void shouldFindAllWithPaginationMappingResults() {
        Account account = validAccount();
        AccountEntity entity = AccountMapper.toEntity(account);
        PageImpl<AccountEntity> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 2), 1);

        when(jpaRepository.findAll(PageRequest.of(0, 2))).thenReturn(page);

        PageResult<Account> result = repository.findAll(0, 2);

        assertEquals(0, result.page());
        assertEquals(2, result.size());
        assertEquals(1, result.totalElements());
        assertEquals(1, result.totalPages());
        assertEquals(1, result.content().size());
    }

    private Account validAccount() {
        Person person = new Person(
                UUID.randomUUID(),
                "Agnes",
                "Galvao",
                new Document(PersonType.PF, "12345678901"),
                "agnes@example.com",
                "Valid@123",
                null,
                null,
                null
        );

        Wallet wallet = new Wallet(
                UUID.randomUUID(),
                Currency.BRL,
                BigDecimal.TEN,
                new HashSet<>(),
                null,
                null
        );

        return new Account(UUID.randomUUID(), person, Set.of(wallet), true, null, null);
    }
}

