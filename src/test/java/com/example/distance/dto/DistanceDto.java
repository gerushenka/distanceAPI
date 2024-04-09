package com.example.distance.dto;

import com.example.distance.dto.DistanceDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DistanceDtoTest {

    @Test
    void testGettersAndSetters() {
        DistanceDto distanceDto = new DistanceDto();

        // Устанавливаем значения
        distanceDto.setId(1L);
        distanceDto.setCityFirst("City1");
        distanceDto.setCitySecond("City2");
        distanceDto.setCityDistance(100.0);

        // Проверяем, что получаем ожидаемые значения
        assertEquals(1L, distanceDto.getId());
        assertEquals("City1", distanceDto.getCityFirst());
        assertEquals("City2", distanceDto.getCitySecond());
        assertEquals(100.0, distanceDto.getCityDistance());
    }
}
