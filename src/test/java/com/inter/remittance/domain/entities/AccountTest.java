package com.inter.remittance.domain.entities;

import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.enums.PersonType;
import com.inter.remittance.domain.valueobjects.Document;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

	@Test
	void shouldThrowWhenPersonIsNull() {
		BusinessException ex = assertThrows(BusinessException.class,
				() -> new Account(
						UUID.randomUUID(),
						null,
						new HashSet<>(),
						true,
						null,
						null
				)
		);

		assertEquals(ErrorCatalog.INVALID_ACCOUNT_PERSON, ex.getMessage());
	}

	@Test
	void shouldThrowWhenWalletsIsNull() {
		BusinessException ex = assertThrows(BusinessException.class,
				() -> new Account(UUID.randomUUID(), validPerson(), null, true, null, null)
		);

		assertEquals(ErrorCatalog.INVALID_ACCOUNT_WALLETS, ex.getMessage());
	}

	@Test
	void shouldGenerateIdAndDefaultActiveWhenNull() {
		Account account = new Account(null, validPerson(), new HashSet<>(), null, null, null);

		assertNotNull(account.id());
		assertTrue(account.isActive());
		assertNull(account.createdAt());
		assertNull(account.updatedAt());
	}

	@Test
	void shouldThrowWhenAddingNullWallet() {
		Account account = new Account(UUID.randomUUID(), validPerson(), new HashSet<>(), true, null, null);

		BusinessException ex = assertThrows(BusinessException.class,
				() -> account.addWallet(null)
		);

		assertEquals(ErrorCatalog.INVALID_ACCOUNT_WALLET, ex.getMessage());
	}

	@Test
	void shouldThrowWhenAddingWalletWithDuplicatedCurrency() {
		Wallet brlWallet = new Wallet(
				UUID.randomUUID(),
				Currency.BRL,
				BigDecimal.ZERO,
				new HashSet<>(),
				null,
				null
		);
		Set<Wallet> wallets = new HashSet<>();
		wallets.add(brlWallet);
		Account account = new Account(
				UUID.randomUUID(),
				validPerson(),
				wallets,
				true,
				null,
				null
		);

		Wallet duplicated = new Wallet(UUID.randomUUID(), Currency.BRL, BigDecimal.TEN, new HashSet<>(), null, null);

		BusinessException ex = assertThrows(BusinessException.class,
				() -> account.addWallet(duplicated)
		);

		assertEquals(ErrorCatalog.INVALID_CURRENCY_WALLET, ex.getMessage());
	}

	@Test
	void shouldAddWalletWithoutMutatingOriginalInstance() {
		Wallet brlWallet = new Wallet(UUID.randomUUID(), Currency.BRL, BigDecimal.ZERO, new HashSet<>(), null, null);
		Set<Wallet> wallets = new HashSet<>();
		wallets.add(brlWallet);
		Account original = new Account(UUID.randomUUID(), validPerson(), wallets, true, null, null);

		Wallet usdWallet = new Wallet(UUID.randomUUID(), Currency.USD, BigDecimal.ZERO, new HashSet<>(), null, null);
		Account updated = original.addWallet(usdWallet);

		assertEquals(1, original.wallets().size());
		assertEquals(2, updated.wallets().size());
		assertTrue(updated.wallets().stream().anyMatch(wallet -> wallet.currency() == Currency.USD));
	}

	private Person validPerson() {
		return new Person(
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
	}

}