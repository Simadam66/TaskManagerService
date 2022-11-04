package com.example.demo.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager myCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("user_cache", "task_cache");
        caffeineCacheManager.setCaffeine(buildCache());
        return caffeineCacheManager;
    }

    private Caffeine<Object,Object> buildCache() {
        return Caffeine.newBuilder()
                .initialCapacity(10)
                .maximumSize(20)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .recordStats();
    }

}
