package com.example.distance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class CacheConfig {
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    @Bean
    public Map<String, Double> distanceCache() {
        return new ConcurrentHashMap<>();
    }
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }


}
