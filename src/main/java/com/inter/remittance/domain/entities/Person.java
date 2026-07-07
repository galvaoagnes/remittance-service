package com.inter.remittance.domain.entities;

import com.inter.remittance.domain.valueobjects.Document;
import com.inter.remittance.domain.commom.ErrorCatalog;
import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.domain.commom.Utils;
import com.inter.remittance.domain.enums.PersonType;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Set;

public record Person(
        UUID id,
        String name,
        String lastName,
        Document document,
        String email,
        String password,
        Set<DailyTransactionLimit> dailyTransactionLimits,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public Person {
        id = (id == null) ? UUID.randomUUID() : id;
        validateName(name);
        validateLastName(lastName);
        validateDocument(document);
        validateEmail(email);
        dailyTransactionLimits = dailyTransactionLimits == null ? DailyTransactionLimit
                                                .setInitialDailyTransactionLimit(
                                                        null,
                                                        document.type()
                                                ): dailyTransactionLimits;
       createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
       updatedAt = updatedAt == null ? LocalDateTime.now() : updatedAt;
    }

    public static Person createNewPerson(
            String name,
            String lastName,
            String documentNumber,
            PersonType type,
            String password,
            String email,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
        validateName(name);
        validateLastName(lastName);
        validateEmail(email);
        validatePassword(password);

       return new Person(
                UUID.randomUUID(),
                name,
                lastName,
                new Document(type,documentNumber),
                email,
                password,
               DailyTransactionLimit
                       .setInitialDailyTransactionLimit(
                               null,
                              type
                       ),
               createdAt,
               updatedAt
        );
    }

    private void validateDocument(Document document) {
        if (document == null) {
            throw new BusinessException(ErrorCatalog.INVALID_DOCUMENT);
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(ErrorCatalog.INVALID_NAME);
        }
    }

    private static void validatePassword(String password) {
        if (password == null || !Utils.isValidPasswordFormat(password)) {
            throw new BusinessException(ErrorCatalog.INVALID_PASSWORD);
        }
    }

    private static void validateLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new BusinessException(ErrorCatalog.INVALID_LAST_NAME);
        }
    }

    private static void validateEmail(String email) {
        if (email == null || !Utils.isValidEmailFormat(email)) {
            throw new BusinessException(ErrorCatalog.INVALID_EMAIL);
        }
    }
}