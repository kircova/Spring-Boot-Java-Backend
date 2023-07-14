package com.midas.springmidas.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ConcurrentMapCache instrumentsCache = new ConcurrentMapCache("instruments");
        instrumentsCache.evict(Duration.ofSeconds(3600)); // Set cache expiration time to 1 hour
        cacheManager.setCaches(Collections.singleton(instrumentsCache));
        return cacheManager;    }
}
