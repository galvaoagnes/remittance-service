package com.inter.remittance.application.service;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class GetCachedValuesService {
    private final CacheManager cacheManager;

    public GetCachedValuesService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public <T> T getCachedValue(String cacheName, Object key, Class<T> type) {
        Cache cache = cacheManager.getCache(cacheName);

        if (cache == null) {
            return null;
        }

        return cache.get(key, type);
    }
}
