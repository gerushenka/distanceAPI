package com.example.distance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class CacheConfig {

    private final Map<String, Double> distanceCache = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Bean
    public Map<String, Double> distanceCache() {
        return distanceCache;
    }

    public void putWithEviction(String key, Double value, int maxCapacity) {
        if (distanceCache.size() >= maxCapacity) {
            String oldestKey = distanceCache.keySet().iterator().next();
            distanceCache.remove(oldestKey);
            logger.info("Evicted from cache: {}", oldestKey);
        }
        logger.info("Adding to cache: {}:{}", key, value);
        distanceCache.put(key, value);
    }

    public Object get(String key) {
        return distanceCache.get(key);
    }

    public void remove(String key) {
        distanceCache.remove(key);
    }

    public void clear() {
        distanceCache.clear();
    }


}
