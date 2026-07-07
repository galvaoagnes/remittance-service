package com.inter.remittance.domain.valueobjects;

import com.inter.remittance.domain.enums.PersonType;

public record Document(PersonType type, String value) {
    public Document {
        type.validateDocument(value);
    }
}