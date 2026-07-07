package com.inter.remittance.infrastructure.persistence.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "person_id", nullable = false, unique = true, updatable = false)
    private PersonEntity personEntity;

    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<WalletEntity> wallets = new HashSet<>();


    @Column(name = "is_active", nullable = false, updatable = true)
    private boolean isActive;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public AccountEntity() {}

    public void addWallet(WalletEntity walletEntity) {
        this.wallets.add(walletEntity);
        walletEntity.setAccount(this);
    }

    public void removeWallet(WalletEntity walletEntity) {
        this.wallets.remove(walletEntity);
        walletEntity.setAccount(null);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PersonEntity getPerson() {
        return personEntity;
    }
    public Set<WalletEntity> getWallets(){
        return this.wallets;
    }

    public void setPerson(PersonEntity personEntity) {
        this.personEntity = personEntity;
    }

    public void setWallets(Set<WalletEntity> walletEntities) {
        this.wallets.clear();

        if (walletEntities != null) {
            walletEntities.forEach(this::addWallet);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void seIsActive(boolean active) {
        isActive = active;
    }

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
