package com.example.distance.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

class CacheConfigTest {

    @Spy
    private CacheConfig cacheConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPutWithEviction() {
        // Given
        String key1 = "key1";
        String key2 = "key2";
        Double value1 = 10.0;
        Double value2 = 20.0;
        int maxCapacity = 1;

        // When
        cacheConfig.putWithEviction(key1, value1, maxCapacity); // вызов метода putWithEviction()
        cacheConfig.putWithEviction(key2, value2, maxCapacity); // вызов метода putWithEviction()

        // Then
        assertNull(cacheConfig.get(key1)); // Проверка, что значение для ключа key1 равно null
        assertEquals(value2, cacheConfig.get(key2)); // Проверка, что значение для ключа key2 соответствует ожидаемому
        verify(cacheConfig, times(1)).putWithEviction(key1, value1, maxCapacity); // Проверка вызова метода putWithEviction с определенными аргументами
        verify(cacheConfig, times(1)).putWithEviction(key2, value2, maxCapacity); // Проверка вызова метода putWithEviction с определенными аргументами
    }




    @Test
    void testRemove() {
        // Given
        String key = "key";
        Double value = 10.0;
        cacheConfig.putWithEviction(key, value, 10);

        // When
        cacheConfig.remove(key);

        // Then
        assertNull(cacheConfig.get(key));
    }

    @Test
    void testClear() {
        // Given
        String key1 = "key1";
        String key2 = "key2";
        Double value1 = 10.0;
        Double value2 = 20.0;
        cacheConfig.putWithEviction(key1, value1, 10);
        cacheConfig.putWithEviction(key2, value2, 10);

        // When
        cacheConfig.clear();

        // Then
        assertNull(cacheConfig.get(key1));
        assertNull(cacheConfig.get(key2));
    }
}
