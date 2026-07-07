package com.inter.remittance.ui.mappers;

import com.inter.remittance.application.command.CreateRemittanceCommand;
import com.inter.remittance.ui.requests.CreateRemittanceRequest;

public final class RemittanceRequestMapper {

    private RemittanceRequestMapper() {
    }

    public static CreateRemittanceCommand toCommand(CreateRemittanceRequest request) {
        return new CreateRemittanceCommand(
                request.sourceAccountId(),
                request.destinationAccountId(),
                request.sourceCurrency(),
                request.destinationCurrency(),
                request.amount()
        );
    }
}