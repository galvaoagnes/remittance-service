package com.inter.remittance.ui.mappers;
import com.inter.remittance.application.command.CreateAccountCommand;
import com.inter.remittance.ui.requests.CreateAccountRequest;

public final class AccountRequestMapper {

    private AccountRequestMapper() {
    }

    public static CreateAccountCommand toCommand(CreateAccountRequest request) {
        if (request == null) {
            return null;
        }

        return new CreateAccountCommand(
                request.name(),
                request.lastName(),
                request.personType(),
                request.documentNumber(),
                request.password(),
                request.email()
        );
    }
}