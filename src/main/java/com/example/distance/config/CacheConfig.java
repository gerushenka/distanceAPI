package com.example.distance.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация кэша.
 */
@Configuration
public class CacheConfig {

  private final Map<String, Double> distanceCache = new ConcurrentHashMap<>();
  private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

  /**
   * Создает и возвращает экземпляр кэша.
   *
   * @return экземпляр кэша
   */
  @Bean
  public Map<String, Double> distanceCache() {
    return distanceCache;
  }

  /**
   * Добавляет элемент в кэш с учетом максимальной емкости.
   *
   * @param key         ключ элемента
   * @param value       значение элемента
   * @param maxCapacity максимальная емкость кэша
   */
  public void putWithEviction(String key, Double value, int maxCapacity) {
    if (distanceCache.size() >= maxCapacity) {
      String oldestKey = distanceCache.keySet().iterator().next();
      distanceCache.remove(oldestKey);
      logger.info("Evicted from cache: {}", oldestKey);
    }
    logger.info("Adding to cache: {}:{}", key, value);
    distanceCache.put(key, value);
  }

  /**
   * Получает элемент из кэша по ключу.
   *
   * @param key ключ элемента
   * @return элемент кэша
   */
  public Object get(String key) {
    return distanceCache.get(key);
  }

  /**
   * Удаляет элемент из кэша по ключу.
   *
   * @param key ключ элемента
   */
  public void remove(String key) {
    distanceCache.remove(key);
  }

  /**
   * Очищает кэш.
   */
  public void clear() {
    distanceCache.clear();
  }
}