package com.inter.remittance.infrastructure.persistence.entities;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "persons", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email", name = "uk_persons_email"),
        @UniqueConstraint(columnNames = "document_value", name = "uk_persons_document_value"),

})
public class PersonEntity {
    @Id
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Embedded
    private DocumentEmbeddable documentEmbeddable;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 150)
    private String email;

    @Column
    private String password;

    @OneToMany(
            mappedBy = "personEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<DailyTransactionLimitEntity> dailyTransactionLimits = new HashSet<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void addDailyTransactionLimit(DailyTransactionLimitEntity limit) {
        dailyTransactionLimits.add(limit);
        limit.setPersonEntity(this);
    }

    public void removeDailyTransactionLimit(DailyTransactionLimitEntity limit) {
        dailyTransactionLimits.remove(limit);
        limit.setPersonEntity(null);
    }

    public PersonEntity(){}

    public PersonEntity(
            UUID id,
            DocumentEmbeddable documentEmbeddable,
            String name,
            String lastName,
            String email,
            String password,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.documentEmbeddable = documentEmbeddable;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public DocumentEmbeddable getDocument() { return documentEmbeddable; }
    public void setDocument(DocumentEmbeddable documentEmbeddable) { this.documentEmbeddable = documentEmbeddable; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UUID getId() {
        return id;
    }

    public Set<DailyTransactionLimitEntity> getDailyTransactionLimits() {
        return dailyTransactionLimits;
    }

    public void setDailyTransactionLimits(Set<DailyTransactionLimitEntity> dailyTransactionLimits) {
        this.dailyTransactionLimits = dailyTransactionLimits;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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