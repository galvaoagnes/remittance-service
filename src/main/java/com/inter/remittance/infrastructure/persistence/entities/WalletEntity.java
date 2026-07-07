package com.inter.remittance.infrastructure.persistence.entities;


import com.inter.remittance.domain.enums.Currency;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "wallets")
public class WalletEntity {

    @Id
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 3)
    private Currency currency;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @OneToMany(
            mappedBy = "sourceWallet",
            cascade = CascadeType.ALL
    )
    private Set<TransactionEntity> transactions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public WalletEntity() {}

    public WalletEntity(
            UUID id,
            Currency currency,
            BigDecimal balance,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
            ) {
        this.id = id;
        this.currency = currency;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }

    public BigDecimal getBanlance() { return balance; }
    public void setBanlance(BigDecimal banlance) { this.balance = banlance; }

    public Set<TransactionEntity> getTransactions() { return transactions; }

    public void setTransactions(Set<TransactionEntity> remittances) { this.transactions = remittances; }

    public AccountEntity getAccount() { return account; }
    public void setAccount(AccountEntity accountEntity) { this.account = accountEntity; }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
