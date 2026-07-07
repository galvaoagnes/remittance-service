package com.inter.remittance.infrastructure.persistence.entities;

import com.inter.remittance.domain.enums.TransactionStatus;
import com.inter.remittance.domain.enums.TransactionType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_source_id")
    private WalletEntity sourceWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_destiny_id")
    private WalletEntity destinationWallet;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransactionType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected TransactionEntity() {
    }

    public TransactionEntity(
            UUID id,
            WalletEntity walletSource,
            WalletEntity destinationWallet,
            BigDecimal amount,
            TransactionStatus status,
            TransactionType type,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
            ) {
        this.id = id;
        this.sourceWallet = walletSource;
        this.destinationWallet = destinationWallet;
        this.amount = amount;
        this.status = status;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public WalletEntity getSourceWallet() {
        return sourceWallet;
    }

    public void setSourceWallet(WalletEntity sourceWallet) {
        this.sourceWallet = sourceWallet;
    }

    public WalletEntity getDestinationWallet() {
        return destinationWallet;
    }

    public void setDestinationWallet(WalletEntity destinationWallet) {
        this.destinationWallet = destinationWallet;
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