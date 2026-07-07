package com.inter.remittance.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCachedValuesServiceTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    private GetCachedValuesService service;

    @BeforeEach
    void setUp() {
        service = new GetCachedValuesService(cacheManager);
    }

    @Test
    void shouldReturnNullWhenCacheDoesNotExist() {
        when(cacheManager.getCache("exchangeRate")).thenReturn(null);

        BigDecimalWrapper value = service.getCachedValue("exchangeRate", "USD", BigDecimalWrapper.class);

        assertNull(value);
        verify(cacheManager).getCache("exchangeRate");
    }

    @Test
    void shouldReturnCachedValueWhenCacheExists() {
        BigDecimalWrapper expected = new BigDecimalWrapper("5.25");
        when(cacheManager.getCache("exchangeRate")).thenReturn(cache);
        when(cache.get("USD", BigDecimalWrapper.class)).thenReturn(expected);

        BigDecimalWrapper result = service.getCachedValue("exchangeRate", "USD", BigDecimalWrapper.class);

        assertEquals(expected, result);
        verify(cache).get("USD", BigDecimalWrapper.class);
    }

    private record BigDecimalWrapper(String value) {
    }
}

