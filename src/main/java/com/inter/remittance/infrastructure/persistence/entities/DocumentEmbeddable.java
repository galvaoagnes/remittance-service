package com.inter.remittance.infrastructure.persistence.entities;

import com.inter.remittance.domain.enums.PersonType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serializable;

@Embeddable
public record DocumentEmbeddable(
        @Enumerated(EnumType.STRING)
        @Column(name = "person_type", length = 20)
        PersonType type,

        @Column(name = "document_value", length = 20)
        String value
) implements Serializable {

    public DocumentEmbeddable {
        type.validateDocument(value);
    }

    public PersonType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}