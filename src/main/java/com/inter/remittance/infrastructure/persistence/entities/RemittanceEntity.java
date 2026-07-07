package com.inter.remittance.infrastructure.persistence.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "remittances")
public class RemittanceEntity {

    @Id
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "transaction_id", nullable = false)
    private TransactionEntity transaction;

    @Column(name = "converted_currency_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal convertedCurrencyAmount;

    @Column(name = "exchange_rate", nullable = false, precision = 19, scale = 8)
    private BigDecimal exchangeRate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected RemittanceEntity() {
    }

    public RemittanceEntity(
            UUID id,
            TransactionEntity transaction,
            BigDecimal originalCurrencyAmount,
            BigDecimal exchangeRate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.transaction= transaction;
        this.convertedCurrencyAmount = originalCurrencyAmount;
        this.exchangeRate = exchangeRate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TransactionEntity getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }

    public BigDecimal getConvertedCurrencyAmount() {
        return convertedCurrencyAmount;
    }

    public void setConvertedCurrencyAmount(BigDecimal convertedCurrencyAmount) {
        this.convertedCurrencyAmount = convertedCurrencyAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
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