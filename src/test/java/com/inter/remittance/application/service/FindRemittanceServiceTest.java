package com.inter.remittance.application.service;

import com.inter.remittance.domain.entities.Remittance;
import com.inter.remittance.domain.repositories.RemittanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindRemittanceServiceTest {

    @Mock
    private RemittanceRepository remittanceRepository;

    private FindRemittanceService service;

    @BeforeEach
    void setUp() {
        service = new FindRemittanceService(remittanceRepository);
    }

    @Test
    void shouldDelegateFindByIdToRepository() {
        UUID remittanceId = UUID.randomUUID();
        Remittance expected = new Remittance(
                remittanceId,
                null,
                java.math.BigDecimal.ONE,
                java.math.BigDecimal.ONE,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );
        when(remittanceRepository.findById(remittanceId)).thenReturn(expected);

        Remittance result = service.findById(remittanceId);

        assertSame(expected, result);
        verify(remittanceRepository).findById(remittanceId);
    }
}

