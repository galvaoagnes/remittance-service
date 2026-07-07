package com.inter.remittance.domain.enums;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionStatusTest {

    @Test
    void shouldReturnNextPossibleStatusesFromCreated() {
        assertEquals(List.of(TransactionStatus.PENDING), TransactionStatus.CREATED.nextPossibleStatuses());
        assertTrue(TransactionStatus.CREATED.canTransitionTo(TransactionStatus.PENDING));
        assertFalse(TransactionStatus.CREATED.canTransitionTo(TransactionStatus.FINISHED_WITH_SUCCESS));
    }

    @Test
    void shouldReturnNextPossibleStatusesFromPending() {
        assertEquals(
                List.of(TransactionStatus.FINISHED_WITH_SUCCESS, TransactionStatus.FINISHED_WITH_FAILURE),
                TransactionStatus.PENDING.nextPossibleStatuses()
        );
        assertTrue(TransactionStatus.PENDING.canTransitionTo(TransactionStatus.FINISHED_WITH_SUCCESS));
        assertTrue(TransactionStatus.PENDING.canTransitionTo(TransactionStatus.FINISHED_WITH_FAILURE));
        assertFalse(TransactionStatus.PENDING.canTransitionTo(TransactionStatus.CREATED));
    }

    @Test
    void shouldReturnNoNextStatusesAfterFinished() {
        assertEquals(List.of(), TransactionStatus.FINISHED_WITH_SUCCESS.nextPossibleStatuses());
        assertEquals(List.of(), TransactionStatus.FINISHED_WITH_FAILURE.nextPossibleStatuses());
        assertFalse(TransactionStatus.FINISHED_WITH_SUCCESS.canTransitionTo(TransactionStatus.PENDING));
        assertFalse(TransactionStatus.FINISHED_WITH_FAILURE.canTransitionTo(TransactionStatus.PENDING));
    }
}

