package com.inter.remittance.application.service;

import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.domain.port.ExchangeGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateExchangeRateServiceTest {

    @Mock
    private ExchangeGateway exchangeGateway;

    private UpdateExchangeRateService service;

    @BeforeEach
    void setUp() {
        service = new UpdateExchangeRateService(exchangeGateway);
    }

    @Test
    void shouldFetchAndReturnExchangeRateFromGateway() {
        when(exchangeGateway.getExchangeRate(Currency.USD)).thenReturn(new BigDecimal("5.20"));

        BigDecimal result = service.execute(Currency.USD);

        assertEquals(0, result.compareTo(new BigDecimal("5.20")));
        verify(exchangeGateway).getExchangeRate(Currency.USD);
    }

    @Test
    void shouldThrowRestClientExceptionInFallback() {
        RestClientException ex = assertThrows(RestClientException.class,
                () -> service.fallback(Currency.USD, new RuntimeException("gateway down")));

        assertEquals("Failed to update exchange rate for currency USD: gateway down", ex.getMessage());
    }
}

