package com.inter.remittance.domain.enums;

import java.util.Collections;
import java.util.List;

public enum TransactionStatus {
    CREATED(),
    PENDING(),
    FINISHED_WITH_SUCCESS(),
    FINISHED_WITH_FAILURE();


    public List<TransactionStatus> nextPossibleStatuses() {
        return switch (this) {
            case CREATED -> List.of(PENDING);
            case PENDING -> List.of(FINISHED_WITH_SUCCESS, FINISHED_WITH_FAILURE);
            case FINISHED_WITH_SUCCESS, FINISHED_WITH_FAILURE -> Collections.emptyList();
        };
    }

    public boolean canTransitionTo(TransactionStatus next) {
        return nextPossibleStatuses().contains(next);
    }

}
