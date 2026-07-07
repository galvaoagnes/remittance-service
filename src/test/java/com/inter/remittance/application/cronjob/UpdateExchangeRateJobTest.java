package com.inter.remittance.application.cronjob;

import com.inter.remittance.application.service.UpdateExchangeRateService;
import com.inter.remittance.domain.enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateExchangeRateJobTest {

    @Mock
    private UpdateExchangeRateService updateExchangeRateService;

    private UpdateExchangeRateJob job;

    @BeforeEach
    void setUp() {
        job = new UpdateExchangeRateJob(updateExchangeRateService);
    }

    @Test
    void shouldUpdateOnlyCurrenciesThatRequireExchangeRate() {
        job.execute();

        verify(updateExchangeRateService).execute(Currency.USD);
        verify(updateExchangeRateService, never()).execute(Currency.BRL);
    }
}

